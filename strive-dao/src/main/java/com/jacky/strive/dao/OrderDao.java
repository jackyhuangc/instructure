package com.jacky.strive.dao;

import com.jacky.strive.dao.model.MemberOrder;
import com.jacky.strive.mapper.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/7 18:38
 * @since jdk1.8
 */
@Mapper
@Repository
public interface OrderDao extends MyMapper<MemberOrder> {

    // TODO 使用tk.mybatis可使dao层结构更简洁
}
