package com.atc.service.impl;

import com.atc.common.criteria.ExpandCriteria;
import com.atc.common.criteria.Restrictions;
import com.atc.common.enums.UserEnum;
import com.atc.common.utils.CodeUtils;
import com.atc.common.utils.Md5Helper;
import com.atc.common.utils.ResultUtil;
import com.atc.common.vo.Result;
import com.atc.common.vo.UserSession;
import com.atc.common.vo.UserVo;
import com.atc.dao.OperationLogRepository;
import com.atc.dao.ProjectInfoRepository;
import com.atc.dao.RegisterInfoRepository;
import com.atc.dao.UserRepository;
import com.atc.dao.entity.OperationLog;
import com.atc.dao.entity.ProjectInfo;
import com.atc.dao.entity.UserInfo;
import com.atc.service.SysService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

@Service
public class SysServiceImpl implements SysService {

    @Resource
    private UserRepository         userRepository;
    @Resource
    private RegisterInfoRepository registerRepository;
    @Resource
    private ProjectInfoRepository  projectInfoRepository;
    @Resource
    private OperationLogRepository operationLogRepository;

    @Override
    public Result register(UserVo userVo) {
        String project = userVo.getProject();
        String startDate = userVo.getStartDate();
        String endDate = userVo.getEndDate();
        int registerCount = registerRepository.countByUserNameAndPhoneAndProjectNameAndBeginTimeAndEndTime(
                userVo.getUserName(), userVo.getPhone(), project, startDate, endDate);
        if (registerCount <= 0) {
            return ResultUtil.error("注册信息未备案!");
        }
        int loginNameCount = userRepository.countByLoginNameAndStatusNot(userVo.getLoginName(), UserEnum.VALIDATE);
        if (loginNameCount > 0) {
            return ResultUtil.error("登陆名称已被使用!");
        }
        int phoneCount = userRepository.countByphoneAndStatusNot(userVo.getPhone(), UserEnum.VALIDATE);
        if (phoneCount > 0) {
            return ResultUtil.error("手机号已被使用!");
        }
        UserInfo save;
        try {
            String passwordMd5 = Md5Helper.MD5(userVo.getPassword());
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(userVo.getUserName());
            userInfo.setLoginName(userVo.getLoginName());
            userInfo.setPhone(userVo.getPhone());
            userInfo.setPassword(passwordMd5);
            userInfo.setCompany(userVo.getCompany());
            userInfo.setStatus(UserEnum.OFFLINE);
            save = userRepository.save(userInfo);
            String pattern = "yyyy-MM-dd HH:mm:ss";
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setProjectName(project);
            Date beginTime = DateUtils.parseDate(startDate + " 00:00:00", pattern);
            Date endTime = DateUtils.parseDate(endDate + " 23:59:59", pattern);
            projectInfo.setBeginTime(beginTime);
            projectInfo.setEndTime(endTime);
            projectInfo.setUserId(save.getUserId());
            projectInfo.setLatitude(userVo.getLatitude());
            projectInfo.setLongitude(userVo.getLongitude());
            projectInfo.setAreaNum(userVo.getAreaNum());
            projectInfoRepository.save(projectInfo);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResultUtil.error("注册失败,请重试!");
        }
        save.setPassword("");
        return ResultUtil.ok(save);
    }

    @Override
    public Result smsCode(UserInfo loginUser) {
        String code = String.valueOf(RandomUtils.nextInt(100000, 1000000));
//        try {
//            SendSmsResponse sendSmsResponse = AliyunSmsUtils.sendMsg(loginUser.getPhone(), code);
//            String successCode = "OK";
//            if (sendSmsResponse.getCode() == null && !successCode.equals(sendSmsResponse.getCode())) {
//                return ResultUtil.error("短信发送失败!");
//            }
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }
        return ResultUtil.ok(code);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserSession login(String loginName, String password) {
        String passwordMd5 = Md5Helper.MD5(password);
        UserInfo userInfo = userRepository.findByLoginNameAndPassword(loginName, passwordMd5);
        if (!ObjectUtils.allNotNull(userInfo)) {
            return null;
        }
        Date crute = new Date();
        ExpandCriteria<ProjectInfo> criteria = new ExpandCriteria<>();
        criteria.add(Restrictions.gte("beginTime", crute, true));
        criteria.add(Restrictions.lte("endTime", crute, true));
        criteria.add(Restrictions.eq("userId", userInfo.getUserId(), true));
        List<ProjectInfo> all = projectInfoRepository.findAll(criteria);
        if (all == null || all.size() < 0) {
            return null;
        }
        int i = userRepository.updateStatusByUserId(userInfo.getUserId(), UserEnum.VALIDATE);
        if (i < 0) return null;
        ProjectInfo projectInfo = all.get(0);
        userInfo.setStatus(UserEnum.VALIDATE);
        OperationLog operationLog = operationLogRepository.findByUserIdAndCloseTimeIsNull(userInfo.getUserId());
        UserSession userSession = new UserSession();
        if (ObjectUtils.allNotNull(operationLog)){
            userSession.setOperationId(operationLog.getOperationId());
        }
        userSession.setUserInfo(userInfo);
        userSession.setProject(projectInfo.getProjectName());
        userSession.setProjectId(projectInfo.getProjectId());
        userSession.setStartDate(DateFormatUtils.format(projectInfo.getBeginTime(), "yyyy-MM-dd"));
        userSession.setEndDate(DateFormatUtils.format(projectInfo.getEndTime(), "yyyy-MM-dd"));
        return userSession;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int registerOk(UserInfo userinfo) {
        int updateCount = userRepository.updateStatusByUserId(userinfo.getUserId(), UserEnum.SUCCESS);
        return updateCount;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result operate(String option, UserSession userSession) {
        UserInfo userInfo = userSession.getUserInfo();
        option = option.toUpperCase();
        if ("CLOSE".equals(option)) {
            userRepository.updateStatusByUserId(userInfo.getUserId(),UserEnum.OFFLINE);
            int operationId = userSession.getOperationId();
            int count = operationLogRepository.updateEndTimeById(operationId, new Date());
            if (count > 0) {
                return ResultUtil.ok();
            }
        } else {
            int projectId = userSession.getProjectId();
            Optional<ProjectInfo> projectInfo = projectInfoRepository.findById(projectId);
            ProjectInfo projectInfo1 = projectInfo.get();
            String randomCode = option.substring(1, 5);
            String coordinate = option.substring(5);
            OperationLog operationLog = new OperationLog();
            operationLog.setUsername(userInfo.getUserName());
            operationLog.setPhone(userInfo.getPhone());
            operationLog.setOpenTime(new Date());
            operationLog.setUserId(userInfo.getUserId());
            operationLog.setExpiryDate(userSession.getStartDate() + "-" + userSession.getEndDate());
            operationLog.setCoordinate(coordinate);
            operationLog.setProjectId(projectId);
            operationLog.setProjectName(projectInfo1.getProjectName());
            OperationLog save = operationLogRepository.save(operationLog);
            userSession.setOperationId(save.getOperationId());
            int areaNum = Integer.valueOf(projectInfo1.getAreaNum());
            int parm2 = Integer.valueOf(randomCode.subSequence(0,2).toString());
            int parm3 = Integer.valueOf(randomCode.subSequence(2,4).toString());
            int return1 = CodeUtils.CRC16_TR(0xffff, (char) areaNum);
            int return2 = CodeUtils.CRC16_TR(return1, (char) parm2);
            int return3 = CodeUtils.CRC16_TR(return2, (char) parm3);
            DecimalFormat df =new DecimalFormat("0000000000000000");
            return ResultUtil.ok(df.format(return3));
        }
        return ResultUtil.error();
    }

    @Override
    public Result map(Integer pageSize, Integer pageNo, UserSession userSession) {
        Map<String, Object> map = new HashMap<>();
        if (!ObjectUtils.allNotNull(pageSize) || pageNo == 0) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<ProjectInfo> all = projectInfoRepository.findAll(pageable);
        map.put("totalCount", all.getTotalElements());
        map.put("data", all.getContent());
        map.put("totalPage", all.getTotalPages());
        return ResultUtil.ok(map);
    }

    @Override
    public Result operationLog(Integer pageSize, Integer pageNo, Integer projectId, UserSession loginUser) {
        Map<String, Object> map = new HashMap<>();
        if (!ObjectUtils.allNotNull(pageSize) || pageNo == 0) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<OperationLog> all = operationLogRepository.findByProjectId(projectId, pageable);
        map.put("totalCount", all.getTotalElements());
        map.put("data", all.getContent());
        map.put("totalPage", all.getTotalPages());
        return ResultUtil.ok(map);
    }
}
