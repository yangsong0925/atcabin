package com.atc.service;

import com.atc.dao.entity.UserInfo;
import com.atc.common.vo.Result;

public interface SysService {


    Result register(UserInfo userinfo);

    Result smsCode(UserInfo loginUser);

    Result login(String loginName, String password , String longitude, String latitude);

    Result registerOk(UserInfo userinfo);
}
