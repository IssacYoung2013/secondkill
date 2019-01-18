package com.issac.seckill.exception;

import com.issac.seckill.result.CodeMsg;

/**
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public class GlobleException extends RuntimeException {

    private CodeMsg cm;

    public GlobleException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
