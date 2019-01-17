package com.issac.seckill.result;

import lombok.Getter;

/**
 *
 * author:  ywy
 * date:    2019-01-16
 * desc:
 */
@Getter
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    public Result(CodeMsg codeMsg) {
        if(codeMsg == null) {
            return;
        }
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    /**
     * 成功调用
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<>(codeMsg);
    }

}
