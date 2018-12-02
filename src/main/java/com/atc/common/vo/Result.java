package com.atc.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "响应结果对象", description = "用户对象user")
public class Result<T> {

    @ApiModelProperty(value = "结果状态码 1:成功 0:失败")
    private String code;
    @ApiModelProperty(value = "提示信息")
    private String msg;
    @ApiModelProperty(value = "响应数据")
    private T data;

}
