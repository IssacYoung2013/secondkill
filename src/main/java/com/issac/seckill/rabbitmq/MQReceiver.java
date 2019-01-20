package com.issac.seckill.rabbitmq;

import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecOrder;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.redis.RedisService;
import com.issac.seckill.service.GoodsService;
import com.issac.seckill.service.OrderService;
import com.issac.seckill.service.SeckillService;
import com.issac.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author:  ywy
 * date:    2019-01-20
 * desc:
 */
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        log.info("Receive Message From Queue: {}", message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        log.info("Receive Topic Queue1 Message {}", message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        log.info("Receive Topic Queue2 Message {}", message);
//    }
//
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE1)
//    public void receiveHeaderQueue(byte[] message) {
//        log.info(" header  queue message:" + new String(message));
//    }

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String message) {
        log.info("Receive Message {}", message);
        SeckillMessage mm = RedisService.stringToBean(message, SeckillMessage.class);
        SecUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId); // 10 个商品 req1 req2
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }

        // 判断是否已经秒杀过了
        SecOrder secOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (secOrder != null) {
            return;
        }
        OrderInfo orderInfo = seckillService.seckill(user, goods);
    }
}
