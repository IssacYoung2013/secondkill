package com.issac.seckill.dao;

import com.issac.seckill.domain.SecUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
@Mapper
public interface SecUserDao {

    @Select("select * from sec_user WHERE id = #{id}")
    SecUser getById(long id);

}
