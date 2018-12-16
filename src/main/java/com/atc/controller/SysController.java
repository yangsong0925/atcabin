package com.atc.controller;

import com.atc.common.enums.UserEnum;
import com.atc.common.utils.ConstantUtils;
import com.atc.common.utils.ResultUtil;
import com.atc.common.vo.Result;
import com.atc.common.vo.UserSession;
import com.atc.common.vo.UserVo;
import com.atc.dao.entity.RegisterInfo;
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
@RequestMapping("sys")
@Slf4j
@Api(description = "后台使用 API ")
public class SysController {

    @Resource
    private SysService sysService;

    @PostMapping("map")
    @ApiOperation(value = "获取每个舱门的基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageNo", value = "页码数", dataType = "int", paramType = "query", required = false)
    })
    public Result map(@RequestParam(name = "pageSize",required = false)Integer pageSize,@RequestParam(name = "pageNo",required = false) Integer pageNo, HttpSession session) {
        Result map = sysService.map(pageSize, pageNo);
        return map;
    }

    @PostMapping("operationLog")
    @ApiOperation(value = "查看日志记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageNo", value = "页码数", dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "projectId", value = "项目id 根据id来获取该舱的操作记录", dataType = "int", paramType = "query", required = true)
})
    public Result operationLog(@RequestParam(name = "pageSize",required = false)Integer pageSize,@RequestParam(name = "pageNo",required = false) Integer pageNo,
                               @RequestParam("projectId") Integer projectId, HttpSession session) {
        Result map = sysService.operationLog(pageSize, pageNo, projectId);
        return map;
    }

    @PostMapping("registerInfo")
    @ApiOperation(value = "注册信息列表", notes = "所有都是必填参数")
    public Result registerInfo(@RequestBody @ApiParam(name = "注册信息列表", value = "", required = true) RegisterInfo registerInfo) {
        if (StringUtils.isBlank(registerInfo.getUserName()) || StringUtils.isBlank(registerInfo.getPhone()) || StringUtils.isBlank(registerInfo.getCompany())
            || StringUtils.isBlank(registerInfo.getProjectName()) || !ObjectUtils.allNotNull(registerInfo.getBeginTime(),registerInfo.getEndTime())) {
            return ResultUtil.error("必填参数不能为空!");
        }
        Result register = sysService.registerInfo(registerInfo);
        return register;
    }

    @PostMapping("userList")
    @ApiOperation(value = "用户信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageNo", value = "页码数", dataType = "int", paramType = "query", required = false)
    })
    public Result userList(@RequestParam(name = "pageSize",required = false)Integer pageSize,@RequestParam(name = "pageNo",required = false) Integer pageNo,
                           @RequestParam(name = "status",required = false) String status, HttpSession session) {
        Result map = sysService.userList(pageSize,pageNo,status);
        return map;
    }

    @PostMapping("changeStatus")
    @ApiOperation(value = "用户信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "改变状态的值", dataType = "int", paramType = "query", required = true)
    })
    public Result changeStatus(@RequestParam("userId")String userId,@RequestParam("status") String status, HttpSession session) {
        Result map = sysService.changeStatus(userId,status);
        return map;
    }

    @PostMapping("registerList")
    @ApiOperation(value = "注册信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", dataType = "int", paramType = "query", required = false),
            @ApiImplicitParam(name = "pageNo", value = "页码数", dataType = "int", paramType = "query", required = false)
    })
    public Result registerList(@RequestParam(name = "pageSize",required = false)Integer pageSize,@RequestParam(name = "pageNo",required = false)Integer pageNo,
                               @RequestParam(name = "status",required = false) String status, HttpSession session) {
        Result map = sysService.registerList(pageSize,pageNo,status);
        return map;
    }

    @PostMapping("login")
    @ApiOperation(value = "获取每个舱门的基础信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "int", paramType = "query", required = true)
    })
    public Result login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password, HttpSession session) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultUtil.error("账户或密码不能为空！");
        }
        UserSession login = sysService.sysLogin(username, password);
        if (!ObjectUtils.allNotNull(login)) {
            return ResultUtil.error("登陆失败！");
        }
        session.setAttribute(ConstantUtils.USER_LOGIN_TOKEN,login);
        return ResultUtil.ok();
    }

}
