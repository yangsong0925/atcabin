package com.atc.common.exception;

import com.atc.common.enums.ExceptionEnum;
import com.atc.common.enums.ResultEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 194906846739586856L;

    protected String code;

    protected String message;

    protected ResultEnum resultEnum;

    protected Object data;

    public BusinessException() {
        ExceptionEnum exceptionEnum = ExceptionEnum.getByEClass(this.getClass());
        if (exceptionEnum != null) {
            resultEnum = exceptionEnum.getResultEnum();
            code = exceptionEnum.getResultEnum().getCode().toString();
            message = exceptionEnum.getResultEnum().getMessage();
        }

    }

    public BusinessException(String message) {
        this();
        this.message = message;
    }

    public BusinessException(String format, Object... objects) {
        this();
        format = StringUtils.replace(format, "{}", "%s");
        this.message = String.format(format, objects);
    }

    public BusinessException(String msg, Throwable cause, Object... objects) {
        this();
        String format = StringUtils.replace(msg, "{}", "%s");
        this.message = String.format(format, objects);
    }

    public BusinessException(ResultEnum resultEnum, Object data) {
        this(resultEnum);
        this.data = data;
    }

    public BusinessException(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
        this.code = resultEnum.getCode().toString();
        this.message = resultEnum.getMessage();
    }

}
