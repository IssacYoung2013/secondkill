package com.issac.seckill.controller;

import com.issac.seckill.domain.SecUser;
import com.issac.seckill.redis.GoodsKey;
import com.issac.seckill.redis.RedisService;
import com.issac.seckill.redis.SecUserKey;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.GoodsService;
import com.issac.seckill.service.SecUserService;
import com.issac.seckill.vo.GoodDetailVo;
import com.issac.seckill.vo.GoodsVo;
import com.issac.seckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String toGoodsList(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            SecUser user) {
        model.addAttribute("user", user);
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        // 查询商品列表
        List<GoodsVo> goodsVoList = goodsService.getGoodsVoList();
        model.addAttribute("goodsList", goodsVoList);

//        return "goods_list";

        WebContext webContext = new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",webContext);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String toGoodsDetail2(
            HttpServletRequest request,
            HttpServletResponse response,
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
//        return "goods_detail";
        String html = redisService.get(GoodsKey.getGoodsDetail,""+goodsId,String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        WebContext webContext = new WebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap());
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",webContext);
        if(!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodDetailVo> toGoodsDetail(
            HttpServletRequest request,
            HttpServletResponse response,
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
        GoodDetailVo vo = new GoodDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSecStatus(secStatus);
        return Result.success(vo);
    }

}
