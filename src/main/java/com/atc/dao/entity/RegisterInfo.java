package com.atc.dao.entity;

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
@Table(name = "t_register_info")
@ApiModel(value = "可注册列表", description = "注册功能在能检索到才能允许注册")
public class RegisterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "可注册信息id", hidden = true)
    private int    registerId;
    @ApiModelProperty(value = "用户名称", required = true)
    private String userName;
    @ApiModelProperty(value = "手机号码", required = true)
    private String phone;
    @ApiModelProperty(value = "注册人公司名称")
    private String company;
    @ApiModelProperty(value = "工程项目名称")
    private String projectName;
    @ApiModelProperty(value = "起始时间")
    private String beginTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
