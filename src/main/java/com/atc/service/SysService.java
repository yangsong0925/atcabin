package com.atc.service;

import com.atc.common.vo.Result;
import com.atc.common.vo.UserSession;
import com.atc.common.vo.UserVo;
import com.atc.dao.entity.RegisterInfo;
import com.atc.dao.entity.UserInfo;

public interface SysService {


    Result register(UserVo userVo);

    Result smsCode(String phone);

    UserSession login(String phone, String password);

    int registerOk(UserInfo userinfo);

    Result operate(String option, UserSession userSession);

    Result map(Integer pageSize, Integer pageNo);

    Result operationLog(Integer pageSize, Integer pageNo, Integer projectId);

    Result registerInfo(RegisterInfo registerInfo);

    Result userList(Integer pageSize, Integer pageNo,String status);

    Result changeStatus(String userId, String status);

    Result registerList(Integer pageSize, Integer pageNo,String status);

    UserSession sysLogin(String username, String password);
}
