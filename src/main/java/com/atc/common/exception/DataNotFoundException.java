package com.atc.common.exception;

import com.atc.common.enums.ResultEnum;

public class DataNotFoundException extends BusinessException {

    private static final long serialVersionUID = 3721036867889297081L;

    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(Object data) {
        super();
        super.data = data;
    }

    public DataNotFoundException(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public DataNotFoundException(ResultEnum resultEnum, Object data) {
        super(resultEnum, data);
    }

    public DataNotFoundException(String msg) {
        super(msg);
    }

    public DataNotFoundException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

}
