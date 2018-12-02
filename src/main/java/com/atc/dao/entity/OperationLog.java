package com.atc.dao.entity;

import com.atc.common.enums.CabinEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;


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
    @ApiModelProperty(value = "操作日志id", hidden = true)
    private int       operationId;
    @ApiModelProperty(value = "机柜状态")
    @Enumerated(EnumType.ORDINAL)
    private CabinEnum status;
    @ApiModelProperty(value = "第一次操作机柜时间")
    private Date      openTime;
    @ApiModelProperty(value = "第二次操作机柜时间")
    private Date      closeTime;
    @ApiModelProperty(value = "用户id", hidden = true)
    private String    userId;
    @ApiModelProperty(value = "项目名称")
    private String    projectName;
    @ApiModelProperty(value = "经度")
    private String    longitude;
    @ApiModelProperty(value = "纬度")
    private String    latitude;

}
