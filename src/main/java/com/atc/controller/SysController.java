package com.atc.controller;

import com.atc.entity.UserInfo;
import com.atc.service.SysService;
import com.atc.common.vo.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public Result smsCode(@PathVariable(name = "userId",required = true) String userId){
        return sysService.smsCode(userId);
    }

}