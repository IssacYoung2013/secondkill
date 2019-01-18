package com.issac.seckill.controller;

import com.issac.seckill.domain.SecUser;
import com.issac.seckill.redis.SecUserKey;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.SecUserService;
import com.issac.seckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    SecUserService userService;

    @RequestMapping("/to_list")
    public String toGoodsList(
//            HttpServletResponse response,
            Model model,
            SecUser user
//                              @CookieValue(value = SecUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
//                              @RequestParam(value = SecUserService.COOKIE_NAME_TOKEN,required = false) String paramToken
    ) {
//        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        SecUser user = userService.getByToken(response,token);

        model.addAttribute("user", user);
        return "goods_list";
    }

}
