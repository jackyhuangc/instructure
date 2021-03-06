package com.jacky.strive.dao;

import com.jacky.strive.dao.model.Member;
import com.jacky.strive.mapper.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface MemberDao extends MyMapper<Member> {

    // TODO 使用tk.mybatis可使dao层结构更简洁
    String getDynamicResult(@Param("sql") String sql);
}
