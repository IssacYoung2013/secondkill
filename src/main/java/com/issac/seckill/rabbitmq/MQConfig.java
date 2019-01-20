package com.issac.seckill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * author:  ywy
 * date:    2019-01-20
 * desc:
 */
@Configuration
public class MQConfig {

    public static final String SECKILL_QUEUE = "seckill_queue";

    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic_queue1";
    public static final String TOPIC_QUEUE2 = "topic_queue2";
    public static final String TOPIC_EXCHANGE = "topic_exchange";
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    public static final String HEADER_EXCHANGE = "header_exchange";
    public static final String HEADER_QUEUE1 = "header_queue1";



    public static final String ROUTEKEY1 = "topic.key1";
    public static final String ROUTEKEY2 = "topic.#";
    // * 代表1个 #代表多个

    /**
     * Direct 模式 交换机 Exchange
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE,true);
    }

    /**
     * Topic模式 交换机
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1,true);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2,true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTEKEY1);
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTEKEY2);
    }

    /**
     * Fanout模式 交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /**
     * Header 模式 交换机
     */
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADER_EXCHANGE);
    }

    @Bean
    public Queue headerQueue1() {
        return new Queue(HEADER_QUEUE1,true);
    }

    @Bean
    public Binding headerBinding() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        return BindingBuilder.bind(headerQueue1()).to(headersExchange()).whereAll(map).match();
    }

    @Bean
    public Queue seckillQueue() {
        return new Queue(SECKILL_QUEUE,true);
    }

}
