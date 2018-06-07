package com.jacky.strive.oauth2.config;

import com.jacky.strive.oauth2.domain.User;
import com.jacky.strive.oauth2.domain.UserMapper;
import com.jacky.strive.service.OauthService;
import com.jacky.strive.service.dto.PrincipalDto;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(CustomUserService.class);

    @Autowired
    OauthService oauthService = new OauthService();

    @Override
    public UserDetails loadUserByUsername(String username) {

        PrincipalDto principal = oauthService.findByUserName(username);
        if (principal == null) {
            logger.info("invalid principal!");
            throw new UsernameNotFoundException("invalid principal!");
        }

        // 添加用户的权限
        final List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        principal.getRoles().stream().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.toString())));

        return new org.springframework.security.core.userdetails.User(username, principal.getPassword(), authorities);
    }

    private UserDetails loadUser(String username) {

        logger.info("重写loadUserByUsername");
        User user = null;
        // user.setInvestorID("0000");
        // user.setPassword("123456");
        try {
            Reader reader = Resources.getResourceAsReader("mybatisconfig.xml");

            // 创建
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            // 解析资源
            SqlSessionFactory factory = builder.build(reader);

            // 打开session
            SqlSession session = factory.openSession();

            // 用接口映射的方式进行CURD操作，官方推荐
            UserMapper userMapper = session.getMapper(UserMapper.class);

            // List<EMP> list = empmapper.queryall();
            user = userMapper.getByUserName(username);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (user == null) {
            logger.info("用户名不存在！");
            throw new UsernameNotFoundException("invalid user!");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        // 用于添加用户的权限。只要把用户权限添加到authorities 就万事大吉。
        authorities.add(new SimpleGrantedAuthority("ADMIN"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);

    }
}