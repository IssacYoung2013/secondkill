package com.issac.seckill.redis;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public class SecUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    public SecUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    private SecUserKey(String prefix) {
        super(prefix);
    }

    public static SecUserKey token = new SecUserKey(TOKEN_EXPIRE,"tk");
    public static SecUserKey getByName = new SecUserKey("name");
    public static SecUserKey getById = new SecUserKey("id");

}
