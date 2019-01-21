package com.issac.seckill.handler;

import java.lang.annotation.*;

/**
 *
 * author:  ywy
 * date:    2019-01-21
 * desc:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NeedLogin {

}
