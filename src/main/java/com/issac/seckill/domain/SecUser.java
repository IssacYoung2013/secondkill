package com.issac.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
@Data
public class SecUser {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
