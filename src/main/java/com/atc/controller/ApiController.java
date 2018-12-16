package com.atc.controller;

import com.atc.common.enums.UserEnum;
import com.atc.common.utils.ConstantUtils;
import com.atc.common.utils.ResultUtil;
import com.atc.common.vo.Result;
import com.atc.common.vo.UserSession;
import com.atc.common.vo.UserVo;
import com.atc.dao.entity.UserInfo;
import com.atc.service.SysService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api")
@Slf4j
@Api(description = "ApiController APP 使用的api")
public class ApiController {

    @Resource
    private SysService sysService;

    @PostMapping("register")
    @ApiOperation(value = "用户注册", notes = "手机号、密码都是必输项 eg:{\"phone\":\"13980152803\",\"password\":\"123\",\"userName\":\"test\"," +
            "\"company\":\"测试项目\",\"project\":\"测试项目\",\"areaNum\":\"1111111\"}")
    public Result register(@RequestBody @ApiParam(name = "用户对象", value = "表单提交参数", required = true) UserVo userVo, @ApiParam(hidden = true) HttpSession session) {
        if (StringUtils.isBlank(userVo.getUserName()) || StringUtils.isBlank(userVo.getPassword()) || StringUtils.isBlank(userVo.getCompany())) {
            return ResultUtil.error("必填参数不能为空!");
        }
        Result register = sysService.register(userVo);
        if (ConstantUtils.ERROR.equals(register.getCode())) {
            return register;
        }
        return ResultUtil.ok();
    }

    @PostMapping("smsCode")
    @ApiOperation(value = "短信验证码获取", notes = "")
    public Result smsCode(@RequestParam(name = "phone") @ApiParam(name = "phone", value = "手机号", required = true) String phone,HttpSession session) {
        String errorMsg = "短信获取失败！";
        try {
            if (!ObjectUtils.allNotNull(phone)) {
                return ResultUtil.error(errorMsg);
            }

            Result result = sysService.smsCode(phone);
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

    @PostMapping("checkSmsCode")
    @ApiOperation(value = "短信验证码校验", notes = " 注册使用短信验证  短信验证码该变用户状态 从未验证 到 可使用权限 暂时先放着")
    @ApiImplicitParam(paramType="query", name = "code", value = "短信验证码", required = true, dataType = "String")
    public Result checkSmsCode(@RequestParam(name = "code") String code, HttpSession session) {
        String errorMsg = "短信验证失败！";
        try {
            Object attribute = session.getAttribute(ConstantUtils.USER_LOGIN_SMS_CODE);
            if (!ObjectUtils.allNotNull(attribute)) {
                return ResultUtil.error(errorMsg);
            }
            String smsCodeSession = String.valueOf(attribute);
            if (smsCodeSession.equals(code)) {
                UserSession loginUser = (UserSession) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
                int i = sysService.registerOk(loginUser.getUserInfo());
                if (i > 0) {
                    loginUser.getUserInfo().setStatus(UserEnum.SUCCESS);
                    session.setAttribute(ConstantUtils.USER_LOGIN_TOKEN,loginUser);
                    session.removeAttribute(ConstantUtils.USER_LOGIN_SMS_CODE);
                    return ResultUtil.ok();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error(errorMsg);
    }

    @PostMapping("login")
    @ApiOperation(value = "登陆 登录名 密码 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query", example = ""),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", example = ""),
            @ApiImplicitParam(name = "code", value = "密码", dataType = "string", paramType = "query", example = "")
    })
    public Result login(@RequestParam(name = "phone") String phone, @RequestParam(name = "password") String password,@RequestParam(name = "code") String code, @ApiParam(hidden = true) HttpSession session) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
            return ResultUtil.error("账户或密码不能为空！");
        }
        UserSession login = sysService.login(phone, password);
        if (!ObjectUtils.allNotNull(login)) {
            return ResultUtil.error("登陆失败！");
        }
//        session.setAttribute(ConstantUtils.USER_LOGIN_TOKEN,login);
        String errorMsg = "短信验证失败！";
        try {
            Object attribute = session.getAttribute(ConstantUtils.USER_LOGIN_SMS_CODE);
            if (!ObjectUtils.allNotNull(attribute)) {
                return ResultUtil.error(errorMsg);
            }
            String smsCodeSession = String.valueOf(attribute);
            if (!smsCodeSession.equals(code)) {
                return ResultUtil.error(errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(errorMsg);
        }
//        UserSession loginUser = (UserSession) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
        int i = sysService.registerOk(login.getUserInfo());
        if (i <= 0) {
            return ResultUtil.error(errorMsg);
        }
        login.getUserInfo().setStatus(UserEnum.SUCCESS);
        session.setAttribute(ConstantUtils.USER_LOGIN_TOKEN,login);
        session.removeAttribute(ConstantUtils.USER_LOGIN_SMS_CODE);
        UserInfo userInfo = login.getUserInfo();
        return ResultUtil.ok();
    }


    @PostMapping("operate")
    @ApiOperation(value = "上传数据接口 (1234)xy=1.1111 2.2222  CLOSE")
    public Result operate(@ApiParam(name = "option", value = "操作码", required = true,example = "(****)xy=#.#### #.#### ****=动态密码 Xy=#.#### #.####=经纬度" )
                           @RequestParam(name = "option") String option, HttpSession session) {
        if (!ObjectUtils.allNotNull(option)) {
            return ResultUtil.error("操作码不能为空！");
        }
        UserSession loginUser = (UserSession) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
        String optionStr = String.valueOf(option);
        Result operate = sysService.operate(optionStr, loginUser);
        if ("CLOSE".equals(optionStr.toUpperCase()) && ConstantUtils.OK.equals(operate.getCode())) {
            session.removeAttribute(ConstantUtils.USER_LOGIN_TOKEN);
            session.invalidate();
        } else {
            session.setAttribute(ConstantUtils.USER_LOGIN_TOKEN, loginUser);
        }
        return operate;
    }

}
