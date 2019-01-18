package com.issac.seckill.util;

import java.util.UUID;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
public class UUIDUtil {
    public static  String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
