package com.atc.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.atc.dao.RegisterInfoRepository;
import com.atc.dao.UserRepository;
import com.atc.entity.UserInfo;
import com.atc.common.enums.UserEnum;
import com.atc.service.SysService;
import com.atc.common.utils.AliyunSmsUtils;
import com.atc.common.utils.ResultUtil;
import com.atc.common.vo.Result;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysServiceImpl implements SysService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private RegisterInfoRepository registerRepository;

    @Override
    public Result register(UserInfo userinfo){
        int registerCount = registerRepository.countByUserNameAndPhone(userinfo.getUserName(),userinfo.getPhone());
        if(registerCount < 0){
            return ResultUtil.error("注册表中不存在信息!");
        }

        userinfo.setStatus(UserEnum.VALIDATE);
        UserInfo save = userRepository.save(userinfo);
        return ResultUtil.success(save);
    }

    @Override
    public Result smsCode(String userId) {
        String code = String.valueOf(RandomUtils.nextInt(100000,1000000));
        UserInfo userInfo = userRepository.findByUserId(userId);
        try {
            int updateCount = userRepository.updateCodeByUserId(userId,code);
            SendSmsResponse sendSmsResponse = AliyunSmsUtils.sendMsg(userInfo.getPhone(), code);
            String successCode = "OK";
            if(sendSmsResponse.getCode() == null && !successCode.equals(sendSmsResponse.getCode())) {
                return ResultUtil.error("短信发送失败!");
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    @Override
    public Result login(String username, String password) {
        return null;
    }
}
