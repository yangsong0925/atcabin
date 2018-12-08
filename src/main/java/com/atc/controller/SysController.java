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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
@Slf4j
@Api(description = "SysController 用户信息存在于session中， 请求会对session中保存信息校验，存在user status = 1 才能成功请求 ")
public class SysController {

    @Resource
    private SysService sysService;

    @PostMapping("register")
    @ApiOperation(value = "用户注册", notes = "手机号、密码都是必输项 eg:{\"phone\":\"13980152803\",\"password\":\"123\",\"userName\":\"test\",\"loginName\":\"ys\"," +
                                    "\"company\":\"测试项目\",\"project\":\"测试项目\",\"startDate\":\"2017-01-01\",\"endDate\":\"2022-01-01\"," +
                                    "\"longitude\":\"1.0000\",\"latitude\":\"2.0000\",\"areaNum\":\"1111111\"}")
    public Result register(@RequestBody @ApiParam(name = "用户对象", value = "表单提交参数", required = true) UserVo userVo, @ApiParam(hidden = true) HttpSession session) {
        if (StringUtils.isBlank(userVo.getUserName()) || StringUtils.isBlank(userVo.getPassword()) || StringUtils.isBlank(userVo.getCompany()) || StringUtils.isBlank(userVo.getLoginName())) {
            return ResultUtil.error("必填参数不能为空!");
        }
        Result register = sysService.register(userVo);
        if (ConstantUtils.ERROR.equals(register.getCode())) {
            return register;
        }
        UserInfo userInfoSave = (UserInfo) register.getData();
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userInfoSave.getUserId());
        result.put("userName", userInfoSave.getUserName());
        result.put("loginName", userInfoSave.getLoginName());
        result.put("phone", userInfoSave.getPhone());
        result.put("company", userInfoSave.getCompany());
        return ResultUtil.ok(result);
    }

    @GetMapping("smsCode")
    @ApiOperation(value = "短信验证码获取", notes = "")
    public Result smsCode(HttpSession session) {
        String errorMsg = "短信获取失败！";
        try {
            UserSession loginUser = (UserSession) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
            UserInfo userInfo = loginUser.getUserInfo();
            Result result = sysService.smsCode(userInfo);
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

    @GetMapping("checkSmsCode/{code}")
    @ApiOperation(value = "短信验证码校验", notes = " 注册使用短信验证  短信验证码该变用户状态 从未验证 到 可使用权限 暂时先放着")
    @ApiImplicitParam(paramType="path", name = "code", value = "短信验证码", required = true, dataType = "String")
    public Result checkSmsCode(@PathVariable(name = "code") String smsCode, HttpSession session) {
        String errorMsg = "短信验证失败！";
        try {
            Object attribute = session.getAttribute(ConstantUtils.USER_LOGIN_SMS_CODE);
            if (!ObjectUtils.allNotNull(attribute)) {
                return ResultUtil.error(errorMsg);
            }
            String smsCodeSession = String.valueOf(attribute);
            if (smsCodeSession.equals(smsCode)) {
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
            @ApiImplicitParam(name = "name", value = "登陆名称", dataType = "string", paramType = "query", example = ""),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "string", paramType = "query", example = ""),
            @ApiImplicitParam(name = "longitude", value = "经度", dataType = "string", paramType = "query", example = ""),
            @ApiImplicitParam(name = "latitude", value = "纬度", dataType = "string", paramType = "query", example = "")
    })
    public Result login(@RequestParam(name = "name") String loginName, @RequestParam(name = "password") String password, @ApiParam(hidden = true) HttpSession session) {
        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
            return ResultUtil.error("账户或密码不能为空！");
        }
        UserSession login = sysService.login(loginName, password);
        if (!ObjectUtils.allNotNull(login)) {
            return ResultUtil.error("登陆失败！");
        }
        login.getUserInfo().setPassword("");
        session.setAttribute(ConstantUtils.USER_LOGIN_TOKEN,login);
        UserInfo userInfo = login.getUserInfo();
        Map<String, Object> result = new HashMap<>();
        result.put("userId", userInfo.getUserId());
        result.put("userName", userInfo.getUserName());
        result.put("phone", userInfo.getPhone());
        result.put("company", userInfo.getCompany());
        result.put("project", login.getProject());
        return ResultUtil.ok(result);
    }


    @GetMapping("operate")
    @ApiOperation(value = "上传数据接口 (1234)xy=1.1111 2.2222  CLOSE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "option", value = "上传数据信息 上传公式返回操作码 close 记录机器关闭离线", dataType = "string",
                    paramType = "query", required = true, example = "(****)xy=#.#### #.#### ****=动态密码 Xy=#.#### #.####=经纬度")
    })
    public Result operate(String option, HttpSession session) {
        UserSession loginUser = (UserSession) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
        Result operate = sysService.operate(option, loginUser);
        if ("CLOSE".equals(option.toUpperCase())) {
            session.removeAttribute(ConstantUtils.USER_LOGIN_TOKEN);
            session.invalidate();
        } else {
            session.setAttribute(ConstantUtils.USER_LOGIN_TOKEN, loginUser);
        }
        return operate;
    }

    @GetMapping("map")
    @ApiOperation(value = "获取每个舱门的基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNo", value = "页码数", dataType = "int", paramType = "query", required = true)
    })
    public Result map(Integer pageSize, Integer pageNo, HttpSession session) {
        UserSession loginUser = (UserSession) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
        Result map = sysService.map(pageSize, pageNo, loginUser);
        return map;
    }

    @GetMapping("operationLog")
    @ApiOperation(value = "查看日志记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNo", value = "页码数", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "projectId", value = "项目id 根据id来获取该舱的操作记录", dataType = "int", paramType = "query", required = true)
    })
    public Result operationLog(Integer pageSize, Integer pageNo, Integer projectId, HttpSession session) {
        UserSession loginUser = (UserSession) session.getAttribute(ConstantUtils.USER_LOGIN_TOKEN);
        Result map = sysService.operationLog(pageSize, pageNo, projectId, loginUser);
        return map;
    }

}
