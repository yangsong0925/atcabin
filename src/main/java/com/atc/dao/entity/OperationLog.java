package com.atc.dao.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "t_operation_log")
@ApiModel(value = "操作日志表", description = "记录操作机柜记录")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "序号", hidden = true)
    private int     operationId;
    //    @ApiModelProperty(value = "机柜状态")
    //    @Enumerated(EnumType.ORDINAL)
    //    private CabinEnum status;
    @ApiModelProperty(value = "手机号")
    private String  phone;
    @ApiModelProperty(value = "姓名")
    private String  username;
    @ApiModelProperty(value = "开启时间")
    private Date    openTime;
    @ApiModelProperty(value = "关闭时间")
    private Date    closeTime;
    @ApiModelProperty(value = "项目名称")
    private String  projectName;
    @ApiModelProperty(value = "项目有效期")
    private String  expiryDate;
    @ApiModelProperty(value = "站点经纬度")
    private String  coordinate;
    @ApiModelProperty(value = "用户id", hidden = true)
    private Integer userId;
    @ApiModelProperty(value = "项目id", hidden = true)
    private Integer projectId;

}
