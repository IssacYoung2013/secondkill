package com.issac.seckill.redis;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public class OrderKey extends BasePrefix {
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
