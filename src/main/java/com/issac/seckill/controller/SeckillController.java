package com.issac.seckill.controller;

import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecOrder;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.GoodsService;
import com.issac.seckill.service.OrderService;
import com.issac.seckill.service.SeckillService;
import com.issac.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * GET POST 区别
     * GET 获取数据 幂等
     * POST 提交数据
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @PostMapping("/do_seckill")
    @ResponseBody
    public Result doSeckill(Model model, SecUser user,
                            @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user",user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId); // 10 个商品 req1 req2
        int stock = goods.getStockCount();
        if(stock <= 0) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        // 判断是否已经秒杀过了
        SecOrder secOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(secOrder != null) {
            return Result.error(CodeMsg.SECKILL_REPEAT);
        }
        OrderInfo orderInfo = seckillService.seckill(user,goods);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);
        return Result.success(orderInfo);
    }
}
