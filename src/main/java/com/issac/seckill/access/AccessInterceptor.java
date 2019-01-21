package com.issac.seckill.access;

import com.alibaba.fastjson.JSON;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.redis.AccessKey;
import com.issac.seckill.redis.RedisService;
import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.result.Result;
import com.issac.seckill.service.SecUserService;
import com.issac.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 *
 * author:  ywy
 * date:    2019-01-21
 * desc:
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    SecUserService userService;

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod) {

            SecUser user = getUser(request,response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod)handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if(needLogin) {
                if(user == null) {
                    render(response,CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getId();
            } else {
                // do nothing
            }
            // 查询访问次数，5秒钟访问5次 martine flower 重构-改善既有代码的设计
            Integer count = redisService.get(AccessKey.withExpire(seconds),key,Integer.class);
            if(count == null) {
                redisService.set(AccessKey.withExpire(seconds),key,1);
            } else if(count < maxCount){
                redisService.incr(AccessKey.withExpire(seconds),key);
            } else {
                render(response,CodeMsg.ACCESS_LIMI_REACHED);
                return false;
            }
        }

        return true;
    }

    private void render(HttpServletResponse response, CodeMsg cm) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private SecUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(SecUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,SecUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;

        return userService.getByToken(response,token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {

        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie :
                cookies) {
            if(cookie.getName().equals(cookieNameToken)) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
