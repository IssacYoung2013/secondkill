package com.issac.seckill.rabbitmq;

import com.issac.seckill.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * author:  ywy
 * date:    2019-01-20
 * desc:
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

//    public void send(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send message {}",msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
//    }
//
//    public void sendTopic(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send topic message {}",msg);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTEKEY1,msg+"1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");
//    }
//
//    public void sendFanout(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send fanout message {}",msg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
//    }
//
//    public void sendHeader(Object message) {
//        String msg = RedisService.beanToString(message);
//        log.info("send header message {}",msg);
//		MessageProperties properties = new MessageProperties();
//		properties.setHeader("header1", "value1");
//		properties.setHeader("header2", "value2");
//		Message obj = new Message(msg.getBytes(), properties);
//		amqpTemplate.convertAndSend(MQConfig.HEADER_EXCHANGE, "", obj);
//	}

    public void sendSeckillMessage(SeckillMessage message) {
        String msg = RedisService.beanToString(message);
        log.info("send message {}",msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE,msg);
    }
}
