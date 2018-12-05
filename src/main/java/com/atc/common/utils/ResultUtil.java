package com.atc.common.utils;

import com.atc.common.vo.Result;

public class ResultUtil {

    public static Result ok(Object data) {
        Result result = new Result();
        result.setCode(ConstantUtils.OK);
        result.setMsg("ok");
        result.setData(data);
        return result;
    }

    public static Result ok() {
        return ok(null);
    }

    public static Result error() {
        Result result = new Result();
        result.setCode(ConstantUtils.ERROR);
        return result;
    }

    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(ConstantUtils.ERROR);
        result.setMsg(msg);
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}
