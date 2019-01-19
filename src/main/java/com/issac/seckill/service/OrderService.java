package com.issac.seckill.service;

import com.issac.seckill.dao.GoodsDao;
import com.issac.seckill.dao.OrderDao;
import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecOrder;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    public SecOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId) {
        return orderDao.getSeckillOrderByUserIdGoodsId(userId,goodsId);
    }

    @Transactional
    public OrderInfo createOrder(SecUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSecPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());

        long orderId =  orderDao.insert(orderInfo);
        orderInfo.setId(orderId);
        SecOrder secOrder = new SecOrder();
        secOrder.setGoodsId(goods.getId());
        secOrder.setOrderId(orderId);
        secOrder.setUserId(user.getId());

        long secOrderId = orderDao.insertSecOrder(secOrder);

        return orderInfo;
    }
}
