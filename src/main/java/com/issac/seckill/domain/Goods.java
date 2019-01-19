package com.issac.seckill.domain;

import lombok.Data;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
