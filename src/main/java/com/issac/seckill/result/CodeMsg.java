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
    public static CodeMsg ERROR = new CodeMsg(500,"error");


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;
}
