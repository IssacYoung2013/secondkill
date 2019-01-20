package com.issac.seckill.service;

import com.issac.seckill.dao.GoodsDao;
import com.issac.seckill.domain.Goods;
import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecOrder;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.redis.RedisService;
import com.issac.seckill.redis.SeckillKey;
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

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo seckill(SecUser user, GoodsVo goods) {
        // 减库存，下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if(success) {
            // order_info sec
            return orderService.createOrder(user,goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    private void setGoodsOver(Long id) {
        redisService.set(SeckillKey.isGoodsOver,""+id,true);
    }

    public long getSeckillResult(Long userId, long goodsId) {
        SecOrder secOrder =  orderService.getSeckillOrderByUserIdGoodsId(userId,goodsId);
        if(secOrder != null) { // 秒杀成功
            return secOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exist(SeckillKey.isGoodsOver,""+goodsId);
    }

    public void reset(List<GoodsVo> goodsVoList) {
        goodsService.resetStock(goodsVoList);
        orderService.deleteOrders();
    }
}
