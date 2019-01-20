package com.issac.seckill.dao;

import com.issac.seckill.domain.Goods;
import com.issac.seckill.domain.SecGoods;
import com.issac.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Mapper
public interface GoodsDao {

    @Select("SELECT g.*,sg.sec_price,sg.stock_count,sg.start_date,sg.end_date FROM sec_goods sg LEFT JOIN goods g ON sg.goods_id = g.id")
    List<GoodsVo> getGoodsVoList();

    @Select("SELECT g.*,sg.sec_price,sg.stock_count,sg.start_date,sg.end_date FROM sec_goods sg LEFT JOIN goods g ON sg.goods_id = g.id WHERE g.id =#{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("UPDATE sec_goods SET stock_count = stock_count - 1 WHERE goods_id = #{id} and stock_count > 0")
    int reduceStock(SecGoods goods);

    @Update("UPDATE sec_goods SET stock_count = 10 WHERE goods_id = #{id} ")
    int resetStock(GoodsVo goodsVo);
}
