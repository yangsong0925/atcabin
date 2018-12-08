package com.atc.service;

import com.atc.common.vo.Result;
import com.atc.common.vo.UserSession;
import com.atc.common.vo.UserVo;
import com.atc.dao.entity.UserInfo;

public interface SysService {


    Result register(UserVo userVo);

    Result smsCode(UserInfo loginUser);

    UserSession login(String loginName, String password);

    int registerOk(UserInfo userinfo);

    Result operate(String option, UserSession userSession);

    Result map(Integer pageSize, Integer pageNo, UserSession userSession);

    Result operationLog(Integer pageSize, Integer pageNo, Integer projectId, UserSession loginUser);
}
