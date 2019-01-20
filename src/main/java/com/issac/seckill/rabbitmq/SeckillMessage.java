package com.issac.seckill.rabbitmq;

import com.issac.seckill.domain.SecUser;
import lombok.Data;

/**
 *
 * author:  ywy
 * date:    2019-01-20
 * desc:
 */
@Data
public class SeckillMessage {
    private SecUser user;
    private long goodsId;
}
