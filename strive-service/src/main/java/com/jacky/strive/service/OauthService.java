package com.jacky.strive.service;

import com.jacky.strive.dao.MemberDao;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.dao.model.Member;
import com.jacky.strive.dao.model.User;
import com.jacky.strive.service.dto.PrincipalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/5 21:09
 * @since jdk1.8
 */
@Service
@Scope("prototype")
public class OauthService {

    private static final String PREFIX_M = "M";
    private static final String PREFIX_U = "U";
    private static final String SEPARATOR = "\\|";

    @Autowired
    UserDao userDao;

    @Autowired
    MemberDao memberDao;

    public PrincipalDto findByUserName(String userName) {

        PrincipalDto principalDto = new PrincipalDto();

        if (userName.split(SEPARATOR).length > 1 && userName.split(SEPARATOR)[1] == PREFIX_M) {
            Member m = new Member();
            m.setMemberName("member");
            m.setMemberPassword("");

            principalDto.setUserName(userName);
            principalDto.setPassword("123456");
            principalDto.setData(m);
        } else {
            User u = new User();
            u.setUsername("admin");
            u.setPassword("");

            principalDto.setUserName(userName);
            principalDto.setPassword("123456");
            principalDto.setData(u);
        }

        return principalDto;
    }
}
