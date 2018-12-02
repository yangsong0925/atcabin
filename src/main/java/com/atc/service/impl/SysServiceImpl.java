package com.atc.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.atc.dao.RegisterInfoRepository;
import com.atc.dao.UserRepository;
import com.atc.dao.entity.UserInfo;
import com.atc.common.enums.UserEnum;
import com.atc.service.SysService;
import com.atc.common.utils.AliyunSmsUtils;
import com.atc.common.utils.ResultUtil;
import com.atc.common.vo.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class SysServiceImpl implements SysService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private RegisterInfoRepository registerRepository;

    @Override
    public Result register(UserInfo userinfo){
        int registerCount = registerRepository.countByUserNameAndPhone(userinfo.getUserName(),userinfo.getPhone());
        if(registerCount <= 0){
            return ResultUtil.error("注册表中不存在信息!");
        }
        int loginNameCount = userRepository.countByLoginName(userinfo.getLoginName());
        if(loginNameCount <= 0){
            return ResultUtil.error("登陆名称已被使用!");
        }
        int phoneCount = userRepository.countByphone(userinfo.getPhone());
        if(phoneCount <= 0){
            return ResultUtil.error("手机号已被使用!");
        }
        String passwordMd5 = DigestUtils.md5Hex(userinfo.getPassword());
        userinfo.setPassword(passwordMd5);
        userinfo.setStatus(UserEnum.VALIDATE);
        UserInfo save = userRepository.save(userinfo);
        save.setPassword("");
        return ResultUtil.success(save);
    }

    @Override
    public Result smsCode(UserInfo loginUser) {
        String code = String.valueOf(RandomUtils.nextInt(100000,1000000));
        try {
            SendSmsResponse sendSmsResponse = AliyunSmsUtils.sendMsg(loginUser.getPhone(), code);
            String successCode = "OK";
            if(sendSmsResponse.getCode() == null && !successCode.equals(sendSmsResponse.getCode())) {
                return ResultUtil.error("短信发送失败!");
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return ResultUtil.success(code);
    }

    @Override
    public Result login(String loginName, String password, String longitude, String latitude) {
        Map<String,Object> result = new HashMap<>();
        String passwordMd5 = DigestUtils.md5Hex(password);
        UserInfo userInfo = userRepository.findByLoginNameAndPassword(loginName,passwordMd5);
        if(!ObjectUtils.allNotNull(userInfo)){
            return ResultUtil.error("用户或者密码错误！");
        }
        userInfo.setPassword("");
        result.put("userInfo",userInfo);
        return null;
    }
}
