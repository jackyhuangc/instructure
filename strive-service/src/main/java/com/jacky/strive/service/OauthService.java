package com.jacky.strive.service;

import com.jacky.common.util.StringUtil;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.dao.model.Member;
import com.jacky.strive.dao.model.User;
import com.jacky.strive.service.dto.PrincipalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    MemberService memberService;

    @Autowired
    UserService userService;

    public PrincipalDto findByUserName(String userName) {
        if (StringUtil.isEmtpy(userName)) {
            return null;
        }
        
        PrincipalDto principalDto = null;
        if (userName.split(SEPARATOR).length > 1 && userName.split(SEPARATOR)[1].equals(PREFIX_M)) {
            Member m = memberService.findByMemberNo(userName.split(SEPARATOR)[0]);
            if (null != m) {
                principalDto = new PrincipalDto<Member>();
                principalDto.setRoles(new ArrayList<>());
                principalDto.setUserName(userName);
                principalDto.setPassword(m.getMemberPassword());
                principalDto.setData(m);
            }
        } else {
            User u = userService.findByUserName(userName.split(SEPARATOR)[0]);
            if (null != u) {
                principalDto = new PrincipalDto<User>();
                principalDto.setRoles(new ArrayList<>());
                principalDto.setUserName(userName);
                principalDto.setPassword(u.getPassword());
                principalDto.setData(u);
            }
        }

        return principalDto;
    }
}
