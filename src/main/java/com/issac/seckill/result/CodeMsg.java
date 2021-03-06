package com.issac.seckill.result;

import lombok.Data;
import lombok.Getter;

/**
 *
 * author:  ywy
 * date:    2019-01-16
 * desc:
 */
@Getter
public class CodeMsg {

    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg ERROR = new CodeMsg(500100,"error");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数校验异常：%s");
    public static CodeMsg REQUEST_ILEGAL = new CodeMsg(500102,"请求非法");
    public static CodeMsg ACCESS_LIMI_REACHED = new CodeMsg(500103,"访问太频繁");



    // 登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210,"Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212,"手机号不能为空");
    public static CodeMsg MOBILE_FORMAT_ERROR = new CodeMsg(500213,"手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXISTS = new CodeMsg(500214,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"密码错误");


    // 订单模块 5004xx
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500401,"订单不存在");

    // 秒杀模块 5005xx
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500,"秒杀完");
    public static CodeMsg SECKILL_REPEAT = new CodeMsg(500501,"不能重复秒杀完");
    public static CodeMsg SECKILL_ERROR = new CodeMsg(500502,"秒杀失败");





    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

    private int code;

    private String msg;
}
