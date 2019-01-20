package com.issac.seckill.controller;

import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.GoodsService;
import com.issac.seckill.service.OrderService;
import com.issac.seckill.vo.GoodsVo;
import com.issac.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * author:  ywy
 * date:    2019-01-20
 * desc:
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @GetMapping("/detail")
    @ResponseBody
    public Result getOrderDetail(SecUser secUser, @RequestParam("orderId") long orderId){
        if(secUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if(orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrderInfo(orderInfo);
        vo.setGood(goods);
        return Result.success(vo);
    }

}
