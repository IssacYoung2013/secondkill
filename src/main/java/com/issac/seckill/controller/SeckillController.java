package com.issac.seckill.controller;

import com.issac.seckill.access.AccessLimit;
import com.issac.seckill.domain.Goods;
import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecOrder;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.rabbitmq.MQSender;
import com.issac.seckill.rabbitmq.SeckillMessage;
import com.issac.seckill.redis.*;
import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.GoodsService;
import com.issac.seckill.service.OrderService;
import com.issac.seckill.service.SeckillService;
import com.issac.seckill.util.MD5Util;
import com.issac.seckill.util.UUIDUtil;
import com.issac.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
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
    @PostMapping("/{path}/do_seckill")
    @ResponseBody
    public Result doSeckill(Model model, SecUser user,
                            @RequestParam("goodsId") long goodsId,
                            @PathVariable("path") String path) {
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

        // 验证path
        boolean ret = seckillService.checkPath(user,goodsId,path);
        if(!ret) {
            return Result.error(CodeMsg.REQUEST_ILEGAL);
        }
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
    @AccessLimit(seconds = 5,maxCount =10,needLogin = true)
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

    @AccessLimit(seconds = 5,maxCount =5,needLogin = true)
    @GetMapping("/path")
    @ResponseBody
    public Result path(Model model,SecUser user     ,
                       HttpServletRequest request,
                       @RequestParam("goodsId") long goodsId,
                       @RequestParam(value = "verifyCode",required = false,defaultValue = "0") int verifyCode) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 查询访问次数，5秒钟访问5次
        String uri = request.getRequestURI();
        String key = uri + "_" + user.getId();
        Integer count = redisService.get(AccessKey.access,key,Integer.class);
        if(count == null) {
            redisService.set(AccessKey.access,key,1);
        } else if(count < 5){
            redisService.incr(AccessKey.access,key);
        } else {
            return Result.error(CodeMsg.ACCESS_LIMI_REACHED);
        }
        boolean check =seckillService.checkVerifyCode(user,goodsId,verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILEGAL);

        }
        String path = seckillService.createSeckillPath(user,goodsId);
        return Result.success(path);
    }

    @GetMapping("/verifyCode")
    @ResponseBody
    public Result getSeckillVerifyCode(HttpServletResponse response, Model model, SecUser user     ,
                                       @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        BufferedImage image = seckillService.createVerifyCode(user,goodsId);
        try {
            OutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SESSION_ERROR);
        }
    }
}
