package com.issac.seckill.redis;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
