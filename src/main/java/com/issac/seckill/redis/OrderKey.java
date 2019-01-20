package com.issac.seckill.redis;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSecOrderByUidGid = new OrderKey("secug");

}
