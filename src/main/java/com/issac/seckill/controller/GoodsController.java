package com.issac.seckill.controller;

import com.issac.seckill.domain.SecUser;
import com.issac.seckill.redis.SecUserKey;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.GoodsService;
import com.issac.seckill.service.SecUserService;
import com.issac.seckill.vo.GoodsVo;
import com.issac.seckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
@Controller
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toGoodsList(
            Model model,
            SecUser user) {
        model.addAttribute("user", user);

        // 查询商品列表
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList", goodsVoList);

        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toGoodsDetail(
            Model model,
            SecUser user,
            @PathVariable("goodsId") long goodsId) {
        // snowflake
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        //
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int secStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {
            // 秒杀还没开始，倒计时
            secStatus = 0;
            remainSeconds = (int)(startAt -now) /1000;
        }
        else if (now > endAt)
        {
            // 秒杀已经结束
            secStatus = 2;
            remainSeconds = -1;

        } else {
            // 秒杀进行中
            secStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secStatus", secStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

}
