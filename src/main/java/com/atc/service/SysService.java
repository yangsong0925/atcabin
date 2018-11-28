package com.atc.service;

import com.atc.dao.entity.UserInfo;
import com.atc.common.vo.Result;

public interface SysService {


    Result register(UserInfo userinfo);

    Result smsCode(UserInfo loginUser);

    Result login(String username, String password , String longitude, String latitude);
}
