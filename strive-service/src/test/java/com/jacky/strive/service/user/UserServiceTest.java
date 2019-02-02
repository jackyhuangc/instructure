package com.jacky.strive.service.user;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.config.DataSourceConfig;
import com.jacky.strive.dao.model.User;
import lombok.Data;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;
import qsq.biz.common.util.StringUtil;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

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

        String[] arr = StringUtils.delimitedListToStringArray("1,2,3,4,", ",");

        boolean ret1 = "1,2,3,4,".endsWith(",");
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
        criteria1.andEqualTo("userName", "admin");
        List<User> users = UserDao.selectByExample(exapl);

        PageInfo<User> pageInfo = new PageInfo<User>(users);

        assert users.size() > 0;
    }

    @Test
    public void testSqlServer() throws SQLException {

        try {
            Reader reader = Resources.getResourceAsReader("mybatisconfig_sql.xml");

            // 创建
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            // 解析资源
            SqlSessionFactory factory = builder.build(reader);

            // 打开session
            SqlSession session = factory.openSession();
            Statement statement = session.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from Broker");

            List<Map> list = convertList(resultSet);

            List<Broker> brokerList = list.stream().collect(Collectors.mapping(s -> {

                Broker broker = new Broker();
                Iterator<Map.Entry<String, String>> it2 = s.entrySet().iterator();
                while (it2.hasNext()) {
                    broker.setBrokerID(it2.next().getKey());
                    Map.Entry map = it2.next();
                    if (map.getKey().equals("BrokerID")) {
                        broker.setBrokerID(it2.next().getValue());
                    }
                    if (map.getKey().equals("BrokerName")) {
                        broker.setBrokerName(it2.next().getValue());
                    }
                }

                return broker;
            }, Collectors.toList()));


            assert null != resultSet;

            // 用接口映射的方式进行CURD操作，官方推荐
            //InvestorMapper invstMapper = session.getMapper(InvestorMapper.class);

            //Investor investor = invstMapper.getById(username);

            //if (investor == null) {
            //  System.out.println("用户名不存在");
            // throw new UsernameNotFoundException("用户名不存在");
            // }

            //List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            // 用于添加用户的权限。只要把用户权限添加到authorities 就万事大吉。
            //authorities.add(new SimpleGrantedAuthority("ADMIN"));

            //return new org.springframework.security.core.userdetails.User(investor.getInvestorID(),
            //        investor.getPassword(), authorities);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // throw new UsernameNotFoundException(e.getMessage());
        }
    }

    private static List convertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        while (rs.next()) {
            Map rowData = new HashMap();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }
}


@Data
class Broker {
    private String brokerID;
    private String brokerName;
}