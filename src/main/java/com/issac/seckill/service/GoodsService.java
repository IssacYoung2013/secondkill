package com.issac.seckill.service;

import com.issac.seckill.dao.GoodsDao;
import com.issac.seckill.domain.Goods;
import com.issac.seckill.domain.SecGoods;
import com.issac.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * author:  ywy
 * date:    2019-01-18
 * desc:
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> getGoodsVoList() {
        return goodsDao.getGoodsVoList();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods) {

        SecGoods g = new SecGoods();
        g.setId(goods.getId());
        g.setStockCount(goods.getStockCount() - 1);
        goodsDao.reduceStock(g);
    }
}
