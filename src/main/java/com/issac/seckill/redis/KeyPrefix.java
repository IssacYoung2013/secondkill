package com.issac.seckill.redis;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
