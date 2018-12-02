package com.atc.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CabinEnum {

    OPEN(0,"开启"),CLOSE(1,"关闭"),UNCLOSE(2,"未关闭");

    private final Integer code;
    private final String value;

    CabinEnum(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static UserEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(UserEnum temp: UserEnum.values()){
            if(temp.getCode().equals(key)){
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
