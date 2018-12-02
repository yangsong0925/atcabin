package com.atc.controller;

import com.atc.common.utils.ConstantUtils;
import com.atc.common.utils.ResultUtil;
import com.atc.common.vo.Result;
import com.atc.dao.entity.UserInfo;
import com.atc.service.SysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
@Slf4j
@Api("SysController")
public class SysController {

    @Resource
    private SysService sysService;

    //    {"phone":"13980152803","password":"测试项目","userName":"杨松","loginName":"杨松","company":"c测试项目"}
    @PostMapping("register")
    @ApiOperation(value = "用户注册", notes = "手机号、密码都是必输项 eg:{\"phone\":\"13980152803\",\"password\":\"测试项目\",\"userName\":\"杨松\",\"company\":\"测试项目\"}")
    public Result register(@RequestBody @ApiParam(name = "用户对象", value = "表单提交参数", required = true) UserInfo userInfo, HttpSession session) {
        if (StringUtils.isBlank(userInfo.getUserName()) || StringUtils.isBlank(userInfo.getPassword()) || StringUtils.isBlank(userInfo.getCompany()) || StringUtils.isBlank(userInfo.getLoginName())) {
            return ResultUtil.error("必填参数不能为空!");
        }
        Result register = sysService.register(userInfo);
        if (ConstantUtils.ERROR.equals(register.getCode())) {
            return register;
        }
        UserInfo userInfoSave = (UserInfo) register.getData();
        session.setAttribute(ConstantUtils.USER_LOGIN_TOKEN, userInfoSave);
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userInfoSave.getUserId());
        result.put("userName", userInfoSave.getUserName());
        result.put("phone", userInfoSave.getPhone());
        result.put("company", userInfoSave.getCompany());
        return ResultUtil.success(result);
    }

    @GetMapping("smsCode")
    public Result smsCode(HttpSession session) {
        String errorMsg = "短信获取失败！";
        try {
            UserInfo loginUser = (UserInfo) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
            Result result = sysService.smsCode(loginUser);
            if (ConstantUtils.ERROR.equals(result.getCode())) {
                return ResultUtil.error(errorMsg);
            }
            session.setAttribute(ConstantUtils.USER_LOGIN_SMS_CODE, result.getData());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error(errorMsg);
    }

    @GetMapping("checkSmsCode/{smsCode}")
    public Result checkSmsCode(@PathVariable(name = "code")String smsCode,HttpSession session) {
        String errorMsg = "短信获取失败！";
        try {
            Object attribute = session.getAttribute(ConstantUtils.USER_LOGIN_SMS_CODE);
            if(!ObjectUtils.allNotNull(attribute) ){
                return ResultUtil.error("短信验证失败!");
            }
            String smsCodeSession = String.valueOf(attribute);
            if(smsCodeSession.equals(smsCode)){

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error(errorMsg);
    }

    @PostMapping("login")
    public Result login(@RequestParam(name = "loginName") String loginName, @RequestParam(name = "password") String password,
                        String longitude, String latitude) {
        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
            return ResultUtil.error("账户或密码不能为空！");
        }
        return sysService.login(loginName, password, longitude, latitude);
    }

}
