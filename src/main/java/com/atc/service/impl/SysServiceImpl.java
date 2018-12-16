package com.atc.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.atc.common.criteria.ExpandCriteria;
import com.atc.common.criteria.Restrictions;
import com.atc.common.enums.RegisterEnum;
import com.atc.common.enums.UserEnum;
import com.atc.common.utils.AliyunSmsUtils;
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
import com.atc.dao.entity.RegisterInfo;
import com.atc.dao.entity.UserInfo;
import com.atc.service.SysService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        String userName = userVo.getUserName();
        String company = userVo.getCompany();
        String phone = userVo.getPhone();
        Date date = new Date();

        // 备案信息验证
        ExpandCriteria<RegisterInfo> criteria = new ExpandCriteria<>();
        criteria.add(Restrictions.eq("userName", userName, false));
        criteria.add(Restrictions.eq("company", company, false));
        criteria.add(Restrictions.eq("projectName", project, false));
        criteria.add(Restrictions.eq("phone", phone, false));
        List<RegisterInfo> registerInfos = registerRepository.findAll(criteria);
        if (registerInfos ==null || registerInfos.size() <= 0) {
            return ResultUtil.error("注册信息未备案!");
        }

        criteria.add(Restrictions.gte("beginTime", date, false));
        criteria.add(Restrictions.lte("endTime", date, false));
        registerInfos = registerRepository.findAll(criteria);
        if (registerInfos ==null || registerInfos.size() <= 0) {
            return ResultUtil.error("备案信息已超出有效期!");
        }

        criteria.add(Restrictions.gte("beginTime", date, false));
        criteria.add(Restrictions.lte("endTime", date, false));
        registerInfos = registerRepository.findAll(criteria);
        if (registerInfos ==null || registerInfos.size() <= 0) {
            return ResultUtil.error("备案信息已超出有效期!");
        }

        criteria.add(Restrictions.lte("status", RegisterEnum.REGISTER, false));
        registerInfos = registerRepository.findAll(criteria);
        if (registerInfos ==null || registerInfos.size() <= 0) {
            return ResultUtil.error("该用户已经注册，请登陆!");
        }

        RegisterInfo registerInfo = registerInfos.get(0);
        registerInfo.setStatus(RegisterEnum.REGISTER);
        registerInfo.setRegisterDate(new Date());
        UserInfo save;
        try {
            //保存用户信息   默认用户状态是  离线
            String passwordMd5 = Md5Helper.MD5(userVo.getPassword());
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(userVo.getUserName());
            userInfo.setPhone(phone);
            userInfo.setPassword(passwordMd5);
            userInfo.setCompany(userVo.getCompany());
            userInfo.setStatus(UserEnum.OFFLINE);
            userInfo.setRegisterId(registerInfo.getRegisterId());
            save = userRepository.save(userInfo);

            // 生成对应项目的  项目信息
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setProjectName(project);
            projectInfo.setBeginTime(registerInfo.getBeginTime());
            projectInfo.setEndTime(registerInfo.getEndTime());
            projectInfo.setUserId(save.getUserId());
            projectInfo.setLatitude(userVo.getLatitude());
            projectInfo.setLongitude(userVo.getLongitude());
            projectInfo.setAreaNum(userVo.getAreaNum());
            projectInfoRepository.save(projectInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("注册失败,请重试!");
        }
        save.setPassword("");
        return ResultUtil.ok(save);
    }

    @Override
    public Result smsCode(String phone) {
        String code = String.valueOf(RandomUtils.nextInt(100000, 1000000));
        try {
            SendSmsResponse sendSmsResponse = AliyunSmsUtils.sendMsg(phone, code);
            String successCode = "OK";
            if (sendSmsResponse.getCode() == null && !successCode.equals(sendSmsResponse.getCode())) {
                return ResultUtil.error("短信发送失败!");
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return ResultUtil.ok(code);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserSession login(String phone, String password) {

        // 密码验证
        String passwordMd5 = Md5Helper.MD5(password);
        UserInfo userInfo = userRepository.findByPhoneAndPassword(phone, passwordMd5);
        if (!ObjectUtils.allNotNull(userInfo)) {
            return null;
        }

        // 加载项目信息，   有限期时间判断
        Date crute = new Date();
        ExpandCriteria<ProjectInfo> criteria = new ExpandCriteria<>();
        criteria.add(Restrictions.gte("beginTime", crute, true));
        criteria.add(Restrictions.lte("endTime", crute, true));
        criteria.add(Restrictions.eq("userId", userInfo.getUserId(), true));
        List<ProjectInfo> all = projectInfoRepository.findAll(criteria);
        if (all == null || all.size() < 0) {
            return null;
        }

//        int i = userRepository.updateStatusByUserId(userInfo.getUserId(), UserEnum.VALIDATE);
//        if (i < 0) return null;


        ProjectInfo projectInfo = all.get(0);
        OperationLog operationLog = operationLogRepository.findByUserIdAndCloseTimeIsNull(userInfo.getUserId());

        UserSession userSession = new UserSession();
        if (ObjectUtils.allNotNull(operationLog)) {
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
        int userId = userInfo.getUserId();
        option = option.toUpperCase();
        if ("CLOSE".equals(option)) {
            //  更新用户状态
            userRepository.updateStatusByUserId(userId, UserEnum.OFFLINE);

            // 更新操作日志   未执行操作    执行了操作
            int operationId = userSession.getOperationId();
            if(StringUtils.isNotEmpty(String.valueOf(operationId))){
                int count = operationLogRepository.updateEndTimeById(operationId, new Date());
                if (count > 0) {
                    return ResultUtil.ok("防盗舱已关闭，已下线!");
                }
            }
            return ResultUtil.ok("未执行操作，已下线!");
        } else {
            int count = operationLogRepository.countByUserIdAndCloseTimeIsNull(userId);
            if(count > 0){
                return ResultUtil.error("存在未关闭防盗舱,请关闭!");
            }
            int projectId = userSession.getProjectId();
            // 获取对应的项目信息
            Optional<ProjectInfo> projectInfo = projectInfoRepository.findById(projectId);
            ProjectInfo projectInfo1 = projectInfo.get();

            String randomCode = option.substring(1, 5);
            String substring = option.substring(option.indexOf('=')+1);
            String x = substring.substring(0,substring.indexOf(' '));
            String y = substring.substring(substring.indexOf(' ')+1);

            // 保存操作日子
            OperationLog operationLog = new OperationLog();
            operationLog.setUsername(userInfo.getUserName());
            operationLog.setPhone(userInfo.getPhone());
            operationLog.setOpenTime(new Date());
            operationLog.setUserId(userId);
            operationLog.setExpiryDate(userSession.getStartDate() + "-" + userSession.getEndDate());
            operationLog.setCoordinate(x+"-"+y);
            operationLog.setProjectId(projectId);
            operationLog.setProjectName(projectInfo1.getProjectName());
            operationLog.setCompany(userInfo.getCompany());
            OperationLog save = operationLogRepository.save(operationLog);

            // 更新最新的  经纬度
            projectInfoRepository.updateById(projectInfo1.getProjectId(),x,y);
            userSession.setOperationId(save.getOperationId());

            // 计算操作码
            int areaNum = Integer.valueOf(projectInfo1.getAreaNum());
            int parm2 = Integer.valueOf(randomCode.subSequence(0, 2).toString());
            int parm3 = Integer.valueOf(randomCode.subSequence(2, 4).toString());
            int return1 = CodeUtils.CRC16_TR(0xffff, (char) areaNum);
            int return2 = CodeUtils.CRC16_TR(return1, (char) parm2);
            int return3 = CodeUtils.CRC16_TR(return2, (char) parm3);
            DecimalFormat df = new DecimalFormat("0000000000000000");
            return ResultUtil.ok(df.format(return3));
        }
    }

    @Override
    public Result map(Integer pageSize, Integer pageNo) {
        if (!ObjectUtils.allNotNull(pageSize) || pageNo == 0) {
            pageNo = 1;
            pageSize = (int)projectInfoRepository.count();
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<ProjectInfo> all = projectInfoRepository.findAll(pageable);
        return ResultUtil.okPage(all);
    }

    @Override
    public Result operationLog(Integer pageSize, Integer pageNo, Integer projectId) {
        if (!ObjectUtils.allNotNull(pageSize) || pageNo == 0) {
            pageNo = 1;
            pageSize = (int)operationLogRepository.count();
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<OperationLog> all = operationLogRepository.findByProjectId(projectId, pageable);
        return ResultUtil.okPage(all);
    }

    @Override
    public Result registerInfo(RegisterInfo registerInfo) {
        ExpandCriteria<RegisterInfo> criteria = new ExpandCriteria<>();
        criteria.add(Restrictions.eq("userName", registerInfo.getUserName(), false));
        criteria.add(Restrictions.eq("company", registerInfo.getCompany(), false));
        criteria.add(Restrictions.eq("projectName", registerInfo.getProjectName(), false));
        criteria.add(Restrictions.eq("phone", registerInfo.getPhone(), false));
        long count = registerRepository.count(criteria);
        if (count > 0){
            return ResultUtil.error("该注册信息已存在!");
        }
        registerInfo.setStatus(RegisterEnum.NOREGISTER);
        registerInfo.setOp(UserEnum.VALIDATE);
        RegisterInfo save = registerRepository.save(registerInfo);
        if(ObjectUtils.allNotNull(save.getRegisterId())){
            return ResultUtil.ok();
        }
        return ResultUtil.error();
    }

    @Override
    public Result userList(Integer pageSize, Integer pageNo,String status) {
        if (!ObjectUtils.allNotNull(pageSize) || pageNo == 0) {
            pageNo = 1;
            pageSize = (int)userRepository.count();
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        ExpandCriteria<UserInfo> criteria = new ExpandCriteria<>();
        if (ObjectUtils.allNotNull(status)){
            UserEnum userEnum = UserEnum.getEnumByKey(status);
            if (ObjectUtils.allNotNull(userEnum)){
                criteria.add(Restrictions.eq("status", userEnum, false));
            }
        }
        Page<UserInfo> all = userRepository.findAll(criteria,pageable);
        return ResultUtil.okPage(all);
    }

    @Override
    public Result changeStatus(String userId, String status) {
        return null;
    }


    @Override
    public Result registerList(Integer pageSize, Integer pageNo,String status) {
        if (!ObjectUtils.allNotNull(pageSize) || pageNo == 0) {
            pageNo = 1;
            pageSize = (int)registerRepository.count();
        }
        ExpandCriteria<RegisterInfo> criteria = new ExpandCriteria<>();
        if (ObjectUtils.allNotNull(status)){
            RegisterEnum enumByKey = RegisterEnum.getEnumByKey(Integer.valueOf(status));
            if (ObjectUtils.allNotNull(enumByKey)){
                criteria.add(Restrictions.eq("status", enumByKey, false));
            }
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<RegisterInfo> all = registerRepository.findAll(criteria,pageable);
        return ResultUtil.okPage(all);
    }

    @Override
    public UserSession sysLogin(String username, String password) {
        // 密码验证
        String passwordMd5 = Md5Helper.MD5(password);
        UserInfo userInfo = userRepository.findByUserNameAndPassword(username, passwordMd5);
        if (ObjectUtils.allNotNull(userInfo)) {
            userInfo.setStatus(UserEnum.SUCCESS);
            UserSession userSession = new UserSession();
            userSession.setUserInfo(userInfo);
            return userSession;
        }
        return null;
    }
}
