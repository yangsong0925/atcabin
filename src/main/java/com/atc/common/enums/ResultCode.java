package com.atc.common.enums;

public enum ResultCode {

    VALIDATE(0,"待验证"),SUCCESS(1,"注册成功");

    private final Integer code;
    private final String message;

    private ResultCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static ResultCode getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ResultCode temp: ResultCode.values()){
            if(temp.getCode().equals(key)){
                return temp;
            }
        }
        return null;
    }
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

}
