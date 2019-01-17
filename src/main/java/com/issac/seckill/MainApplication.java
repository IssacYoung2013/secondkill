package com.issac.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * author:  ywy
 * date:    2019-01-16
 * desc:
 */
@SpringBootApplication(scanBasePackages = "com.issac.seckill")
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);
    }
}
