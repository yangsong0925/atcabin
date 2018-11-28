package com.atc.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {

    private String code;
    private String msg;
    private T data;

}
