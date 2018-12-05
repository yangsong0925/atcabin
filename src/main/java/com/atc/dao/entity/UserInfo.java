package com.atc.dao.entity;

import com.atc.common.annotation.Phone;
import com.atc.common.enums.UserEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "用户信息", description = "用户对象user")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "用户id", hidden = true)
    private int      userId;

    @ApiModelProperty(value = "用户名称", required = true)
    private String   userName;

    @ApiModelProperty(value = "登陆名称", required = true)
    private String   loginName;

    @ApiModelProperty(value = "用户密码", required = true)
    private String   password;

    @Phone
    @ApiModelProperty(value = "手机号码", required = true)
    private String   phone;

    @ApiModelProperty(value = "公司名称")
    private String   company;

    @ApiModelProperty(value = "用户状态", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @Enumerated(EnumType.ORDINAL)
    private UserEnum status;

}


