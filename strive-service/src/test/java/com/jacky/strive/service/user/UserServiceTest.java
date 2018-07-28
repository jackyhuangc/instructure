package com.jacky.strive.service.user;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.config.DataSourceConfig;
import com.jacky.strive.dao.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import qsq.biz.common.util.StringUtil;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/8 11:09
 * @since jdk1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceConfig.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserDao UserDao;

    @Test
    public void testMybatis() {

        String maxUserId = "U0003";
        String rett = maxUserId.substring(1);
        if (!StringUtil.isEmtpy(maxUserId)) {
            maxUserId = "U" + String.format("%04d", Integer.valueOf(Integer.parseInt(maxUserId.substring(1)) + 1));
        }
        String ret = String.format("%04d", 10);

        assert null != ret;
     /*   Page<User> page = PageHelper.startPage(2, 2);
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo("admin");
        List<User> users = IUserDao.selectByExample(example);

        PageInfo<User> pageInfo = new PageInfo<User>(users);

        assert users.size() > 0;*/
    }

    @Test
    public void testTkMybatis() {
        Page<User> page = PageHelper.startPage(2, 2);
        Example exapl = new Example(User.class);
        Example.Criteria criteria1 = exapl.createCriteria();
        criteria1.andEqualTo("username", "admin");
        List<User> users = UserDao.selectByExample(exapl);

        PageInfo<User> pageInfo = new PageInfo<User>(users);

        assert users.size() > 0;
    }
}