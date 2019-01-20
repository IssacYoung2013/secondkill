package com.issac.seckill.service;

import com.issac.seckill.dao.GoodsDao;
import com.issac.seckill.domain.Goods;
import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo seckill(SecUser user, GoodsVo goods) {
        // 减库存，下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        // order_info sec
        return orderService.createOrder(user,goods);
    }
}
