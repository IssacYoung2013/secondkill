package com.issac.seckill.dao;

import com.issac.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * author:  ywy
 * date:    2019-01-17
 * desc:
 */
@Mapper
public interface UserDao {

    @Select("select * FROM  USER WHERE id = #{id}")
    User getById(@Param("id") int id);

    @Insert("insert into user(id,name) VALUES (#{id},#{name})")
    int insert(User user);
}
