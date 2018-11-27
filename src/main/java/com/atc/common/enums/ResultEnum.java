package com.atc.common.enums;

public enum ResultEnum {

    PARAM_IS_INVALID(300,"无效参数！"),
    DATA_ALREADY_EXISTED(300,"数据已存在！"),
    PERMISSION_NO_ACCESS(300,"无访问权限！"),
    SYSTEM_INNER_ERROR(300,"系统内部错误！"),
    RESULE_DATA_NONE(300,"数据找不到！");

    private final Integer code;
    private final String message;

    private ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static ResultEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ResultEnum temp: ResultEnum.values()){
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
