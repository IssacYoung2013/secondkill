package com.issac.seckill.controller;

import com.issac.seckill.domain.SecUser;
import com.issac.seckill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * author:  ywy
 * date:    2019-01-19
 * desc:
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/info")
    @ResponseBody
    public Result<SecUser> info(Model model, SecUser user) {
        return Result.success(user);
    }
}
