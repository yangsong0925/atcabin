package com.atc.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RegisterEnum {
    NOREGISTER(0, "未注册"), REGISTER(1, "已注册");

    private final Integer code;
    private final String  value;

    RegisterEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static RegisterEnum getEnumByKey(Integer key) {
        if (null == key) {
            return null;
        }
        for (RegisterEnum temp : RegisterEnum.values()) {
            if (temp.getCode() == key) {
                return temp;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
