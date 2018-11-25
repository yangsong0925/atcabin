package com.atc.common.enums;

public enum UserType {

    VALIDATE(0,"待验证"),SUCCESS(1,"注册成功");

    private final Integer code;
    private final String value;

    private UserType(Integer code, String value){
        this.code = code;
        this.value = value;
    }

    public static UserType getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(UserType temp: UserType.values()){
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
