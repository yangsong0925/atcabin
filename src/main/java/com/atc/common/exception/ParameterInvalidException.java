package com.atc.common.exception;

import com.atc.common.enums.ResultEnum;

public class ParameterInvalidException extends BusinessException {

    public ParameterInvalidException() {
        super();
    }

    public ParameterInvalidException(Object data) {
        super();
        super.data = data;
    }

    public ParameterInvalidException(ResultEnum resultEnum) {
        super(resultEnum);
    }

    public ParameterInvalidException(ResultEnum resultEnum, Object data) {
        super(resultEnum, data);
    }

    public ParameterInvalidException(String msg) {
        super(msg);
    }

    public ParameterInvalidException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

}
