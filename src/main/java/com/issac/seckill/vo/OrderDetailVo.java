package com.issac.seckill.vo;

import com.issac.seckill.domain.OrderInfo;
import lombok.Data;

/**
 *
 * author:  ywy
 * date:    2019-01-20
 * desc:
 */
@Data
public class OrderDetailVo {
    private GoodsVo good;
    private OrderInfo orderInfo;
}
