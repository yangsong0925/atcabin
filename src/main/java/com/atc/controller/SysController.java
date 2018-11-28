package com.atc.controller;

import com.atc.common.utils.ConstantUtils;
import com.atc.common.utils.ResultUtil;
import com.atc.dao.entity.UserInfo;
import com.atc.service.SysService;
import com.atc.common.vo.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api")
@Slf4j
@Api("SysController")
public class SysController {

    @Resource
    private SysService sysService;

    @PostMapping("login")
    public UserInfo login(){
        return null;
    }

    @PostMapping("register")
    public Result register(UserInfo userInfo){
        return sysService.register(userInfo);
    }

    @PostMapping("smsCode/{userId}")
    public Result smsCode(@PathVariable(name = "userId") String userId, HttpSession session){
        if(StringUtils.isEmpty(userId)){
            return ResultUtil.error("参数异常！");
        }
        String errorMsg = "短信获取失败！";
        try {
            UserInfo loginUser = (UserInfo) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
            Result result = sysService.smsCode(loginUser);
            if(ConstantUtils.ERROR.equals(result.getCode())){
                return ResultUtil.error(errorMsg);
            }
            session.setAttribute(ConstantUtils.USER_LOGIN_SMS_CODE,result.getData());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error(errorMsg);
    }

    @PostMapping("login")
    public Result login(@RequestParam(name="username") String username ,@RequestParam(name="password")  String password,
                        String longitude,String latitude){
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResultUtil.error("账户或密码不能为空！");
        }
        return sysService.login( username ,password,longitude,latitude);
    }

}
