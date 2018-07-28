package com.jacky.strive.dao;

import com.jacky.strive.mapper.MyMapper;
import com.jacky.strive.dao.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/7 18:38
 * @since jdk1.8
 */
@Mapper
@Repository
public interface UserDao extends MyMapper<User> {

    // TODO 使用tk.mybatis可使dao层结构更简洁

    // @Select("SELECT * FROM USER WHERE username like '%${username}%'")
    List<User> findByUserName(@Param("username") String username);

    String getDynamicResult(@Param("sql") String sql);
}
