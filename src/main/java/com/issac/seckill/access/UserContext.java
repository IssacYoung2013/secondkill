package com.issac.seckill.access;

import com.issac.seckill.domain.SecUser;

/**
 *
 * author:  ywy
 * date:    2019-01-21
 * desc:
 */
public class UserContext {

    private static ThreadLocal<SecUser> userHolder = new ThreadLocal<>();

    public static void setUser(SecUser user) {
        userHolder.set(user);
    }

    public static SecUser getUser() {
        return userHolder.get();
    }
}
