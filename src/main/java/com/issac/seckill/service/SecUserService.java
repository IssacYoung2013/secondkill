package com.issac.seckill.service;

import com.issac.seckill.dao.SecUserDao;
import com.issac.seckill.domain.SecUser;
import com.issac.seckill.exception.GlobleException;
import com.issac.seckill.redis.RedisService;
import com.issac.seckill.redis.SecUserKey;
import com.issac.seckill.result.CodeMsg;
import com.issac.seckill.util.MD5Util;
import com.issac.seckill.util.UUIDUtil;
import com.issac.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
@Service
public class SecUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    SecUserDao secUserDao;

    @Autowired
    RedisService redisService;

    public SecUser getById(long id) {
        // 取缓存
        SecUser secUser = redisService.get(SecUserKey.getById, "" + id, SecUser.class);
        if (secUser != null) {
            return secUser;
        }
        // 取数据库
        secUser = secUserDao.getById(id);
        if (secUser != null) {
            redisService.set(SecUserKey.getById, "" + id, secUser);
        }
        return secUser;
    }

    public boolean updatePassword(String token, long id, String formPass) {

        // 取user对象
        SecUser secUser = getById(id);
        if (secUser == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXISTS);
        }

        // 更新数据库
        SecUser toBeUpdate = new SecUser();
        secUser.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, secUser.getSalt()));
        secUserDao.update(toBeUpdate);

        // 处理缓存
        redisService.delete(SecUserKey.getById, "" + id);

        // 更新token
        secUser.setPassword(toBeUpdate.getPassword());
        redisService.set(SecUserKey.token, token, secUser);
        return true;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobleException(CodeMsg.ERROR);
        }

        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        // 判断手机号是否存在
        SecUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXISTS);
        }

        // 验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();

        // 生成cookie
        addCookie(response, token, user);

        return token;
    }

    public SecUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SecUser user = redisService.get(SecUserKey.token, token, SecUser.class);
        // 延长有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }

    private void addCookie(HttpServletResponse response, String token, SecUser user) {
        // 生成cookie
        redisService.set(SecUserKey.token, token, user);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SecUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
