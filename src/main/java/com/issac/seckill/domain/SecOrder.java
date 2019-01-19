package com.issac.seckill.domain;

import lombok.Data;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Data
public class SecOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
}
