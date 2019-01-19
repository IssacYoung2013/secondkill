package com.issac.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
