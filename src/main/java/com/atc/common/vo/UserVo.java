package com.atc.common.vo;

import com.atc.dao.entity.UserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@ApiModel(value = "用户信息请求", description = "用户对象user")
public class UserVo extends UserInfo {

    @ApiModelProperty(value = "项目名字", required = true)
    private String   project;

    @ApiModelProperty(value = "开始时间", required = true)
    private String   startDate;

    @ApiModelProperty(value = "结束时间", required = true)
    private String   endDate;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "区号")
    private String areaNum;
}
