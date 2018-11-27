package com.atc.entity;

import com.atc.common.enums.UserEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "sys_userinfo")
public class UserInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int      userId;
    private String   userName;
    private String   password;
    private String   phone;
    private String   company;
    @Enumerated(EnumType.ORDINAL)
    private UserEnum status;
    private String   smsCode;

}


