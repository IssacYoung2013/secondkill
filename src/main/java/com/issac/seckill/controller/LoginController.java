package com.issac.seckill.controller;

import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.SecUserService;
import com.issac.seckill.util.ValidatorUtil;
import com.issac.seckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    SecUserService userService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "/do_login", method = RequestMethod.POST)
    @ResponseBody
    public Result doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());

        // 登录
        userService.login(response, loginVo);
        return Result.success(true);
    }
}
