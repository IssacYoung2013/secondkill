package com.issac.seckill.dao;

import com.issac.seckill.domain.OrderInfo;
import com.issac.seckill.domain.SecOrder;
import com.issac.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Mapper
public interface OrderDao {

    @Select("SELECT * FROM sec_order WHERE user_id = #{userId} AND  goods_id =#{goodsId}")
    SecOrder getSeckillOrderByUserIdGoodsId(@Param("userId") long userId,
                                            @Param("goodsId") long goodsId);

    @Insert("INSERT INTO order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date) VALUES " +
            "(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("INSERT INTO sec_order(user_id,goods_id,order_id) VALUES (#{userId},#{goodsId},#{orderId})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    long insertSecOrder(SecOrder secOrder);
}
