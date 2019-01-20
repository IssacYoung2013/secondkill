package com.issac.seckill.vo;

import com.issac.seckill.domain.SecUser;
import lombok.Data;

/**
 *
 * author:  ywy
 * date:    2019-01-19
 * desc:
 */
@Data
public class GoodDetailVo {

    private int secStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private SecUser user;

}
