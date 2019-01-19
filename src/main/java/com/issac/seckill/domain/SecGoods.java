package com.issac.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Data
public class SecGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
