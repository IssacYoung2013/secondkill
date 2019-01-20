package com.issac.seckill.controller;

import com.issac.seckill.domain.Goods;
import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecOrder;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.rabbitmq.MQSender;
import com.issac.seckill.rabbitmq.SeckillMessage;
import com.issac.seckill.redis.GoodsKey;
import com.issac.seckill.redis.OrderKey;
import com.issac.seckill.redis.RedisService;
import com.issac.seckill.redis.SeckillKey;
import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.GoodsService;
import com.issac.seckill.service.OrderService;
import com.issac.seckill.service.SeckillService;
import com.issac.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goods :
                goodsVoList) {
            redisService.set(GoodsKey.getGoodsStock, "" + goods.getId(), goods.getStockCount());
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * GET POST 区别
     * GET 获取数据 幂等
     * POST 提交数据
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @PostMapping("/do_seckill")
    @ResponseBody
    public Result doSeckill(Model model, SecUser user,
                            @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
//        //判断库存
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId); // 10 个商品 req1 req2
//        int stock = goods.getStockCount();
//        if(stock <= 0) {
//            return Result.error(CodeMsg.SECKILL_OVER);
//        }
//
//        // 判断是否已经秒杀过了
//        SecOrder secOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
//        if(secOrder != null) {
//            return Result.error(CodeMsg.SECKILL_REPEAT);
//        }
//        OrderInfo orderInfo = seckillService.seckill(user,goods);
//        model.addAttribute("orderInfo",orderInfo);
//        model.addAttribute("goods",goods);
//        return Result.success(orderInfo);

        // 内存标记，减少Redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        // 预减库存
        long stock = redisService.decr(GoodsKey.getGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        // 判断是否已经秒杀到了
        SecOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.SECKILL_REPEAT);
        }

        // 入队
        SeckillMessage mm = new SeckillMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(mm);

        return Result.success(0); // 排队中
    }

    /**
     * orderId 成功
     * -1 库存不足 秒杀失败
     * 0 排队中
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping("/result")
    @ResponseBody
    public Result seckillResult(Model model, SecUser user,
                                @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(orderId);
    }

    @GetMapping("/reset")
    @ResponseBody
    public Result reset(Model model) {

        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        for (GoodsVo goods :
                goodsVoList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getGoodsStock,""+goods.getId(),10);
            localOverMap.put(goods.getId(),false);
        }

        redisService.delete(OrderKey.getSecOrderByUidGid);
        redisService.delete(SeckillKey.isGoodsOver);
        seckillService.reset(goodsVoList);
        return Result.success(true);
    }
}
