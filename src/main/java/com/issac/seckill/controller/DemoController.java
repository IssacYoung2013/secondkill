package com.issac.seckill.controller;

import com.issac.seckill.domain.User;
import com.issac.seckill.rabbitmq.MQSender;
import com.issac.seckill.redis.RedisService;
import com.issac.seckill.redis.UserKey;
import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * author:  ywy
 * date:    2019-01-16
 * desc:
 */
@Controller
public class DemoController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    MQSender mqSender;

    @GetMapping("/home")
    String home() {
        return "hello world !";
    }
    // 1. rest api 2 页面

    @GetMapping("/hello")
    Result hello() {
        return Result.success("hello,world");
    }

    @GetMapping("/hello2")
    Result hello2() {
        return Result.error(CodeMsg.ERROR);
    }

    @GetMapping("/thymeleaf")
    String thymeleaf(Model model) {
        model.addAttribute("name","Issac");
        return "hello";
    }

    @GetMapping("/db/get")
    @ResponseBody
    Result dbGet(@RequestParam("id") int id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/db/tx")
    @ResponseBody
    Result dbTx() {
        return Result.success(userService.tx());
    }

    @GetMapping("/redis/get")
    @ResponseBody
    Result redisGet() {
        User user = redisService.get(UserKey.getById,"" + 1,User.class);
        return Result.success(user);
    }

    @GetMapping("/redis/set")
    @ResponseBody
    Result redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("1111");
        boolean ret = redisService.set(UserKey.getById,"" + 1,user);
        return Result.success(true);
    }

//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result mq() {
//        mqSender.send("Hello World");
//        return Result.success(true);
//    }
//
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result topicmq() {
//        mqSender.sendTopic("Hello World");
//        return Result.success(true);
//    }
//
//    // swagger
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result fanoutmq() {
//        mqSender.sendFanout("Hello World");
//        return Result.success(true);
//    }
//
//    @RequestMapping("/mq/header")
//    @ResponseBody
//    public Result headermq() {
//        mqSender.sendHeader("Hello World");
//        return Result.success(true);
//    }

}
