package com.issac.seckill.vo;

import com.issac.seckill.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Data
public class GoodsVo extends Goods {
    private Double secPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
