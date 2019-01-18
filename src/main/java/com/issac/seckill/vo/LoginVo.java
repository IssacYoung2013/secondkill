package com.issac.seckill.vo;

import com.issac.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
@Data
public class LoginVo {

    @NotNull
    @IsMobile(required = true)
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
