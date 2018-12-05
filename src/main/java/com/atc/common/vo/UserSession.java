package com.atc.common.vo;


import com.atc.dao.entity.UserInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserSession {

    private UserInfo userInfo;
    private String   smsCode;
    private String   project;
    private String   startDate;
    private String   endDate;
    private int      operationId;
    private int      projectId;

}
