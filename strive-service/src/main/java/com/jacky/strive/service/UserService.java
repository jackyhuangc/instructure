package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/5 21:09
 * @since jdk1.8
 */
@Service
@Scope("prototype")
public class UserService {

    @Autowired
    UserDao userDao;

    public String testConnnect() {
        Page<User> page = PageHelper.startPage(2, 2);
        Example exapl = new Example(User.class);
        Example.Criteria criteria1 = exapl.createCriteria();
        criteria1.andEqualTo("username", "admin");
        List<User> users = userDao.selectByExample(exapl);

        PageInfo<User> pageInfo = new PageInfo<User>(users);

        return users.size() > 0 ? users.get(0).getUsername() : "Success！";
    }
}
