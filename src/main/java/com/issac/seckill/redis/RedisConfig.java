package com.issac.seckill.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author:  ywy
 * date:    2019-01-17
 * desc:
 */
@Component
@ConfigurationProperties(prefix="redis")
@Data
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;//秒
    private String password;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait;//秒
}
