package com.issac.seckill.redis;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
public class SeckillKey extends BasePrefix {


    public SeckillKey(String prefix) {
        super(prefix);
    }

    public SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey(0,"igo");
    public static SeckillKey getSeckillPath = new SeckillKey(60,"sp");
    public static SeckillKey getSeckillVerifyCode = new SeckillKey(300,"vc");


}
