package com.issac.seckill.controller;

import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecOrder;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.service.GoodsService;
import com.issac.seckill.service.OrderService;
import com.issac.seckill.service.SeckillService;
import com.issac.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RequestMapping("/do_seckill")
    public String doSeckill(Model model, SecUser user,
                            @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user",user);
        if(user == null) {
            return "login";
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER);
            return "seckill_fail";
        }

        // 判断是否已经秒杀过了
        SecOrder secOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(secOrder != null) {
            model.addAttribute("errmsg", CodeMsg.SECKILL_REPEAT);
            return "seckill_fail";
        }
        OrderInfo orderInfo = seckillService.seckill(user,goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);
        return "order_detail";
    }
}
