package com.atc.service;

import com.atc.entity.UserInfo;
import com.atc.common.vo.Result;

public interface SysService {


    Result register(UserInfo userinfo);

    Result smsCode(String userId);

}
