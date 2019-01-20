package com.issac.seckill.config;

import com.issac.seckill.domain.SecUser;
import com.issac.seckill.service.SecUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {


    @Autowired
    private SecUserService userService;
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz =  methodParameter.getParameterType();
        return clazz == SecUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

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
