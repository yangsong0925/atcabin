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
@Table(name = "t_project_info")
@ApiModel(value = "项目信息表", description = "分离项目,用户表确保一个人有多个项目的情况")
public class ProjectInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "项目id", hidden = true)
    private int    projectId;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "起始时间")
    private Date   beginTime;
    @ApiModelProperty(value = "结束时间")
    private Date   endTime;
    @ApiModelProperty(value = "用户id", hidden = true)
    private Integer userId;
    @ApiModelProperty(value = "经度")
    private String longitude;
    @ApiModelProperty(value = "纬度")
    private String latitude;


}
