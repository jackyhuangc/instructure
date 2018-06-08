package com.jacky.strive.oauth2.config;

import com.jacky.strive.service.OauthService;
import com.jacky.strive.service.dto.PrincipalDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(CustomUserService.class);

    @Autowired
    OauthService oauthService;

    @Override
    public UserDetails loadUserByUsername(String username) {

        PrincipalDto principal = oauthService.findByUserName(username);
        if (null == principal) {
            logger.info("invalid principal!");
            throw new UsernameNotFoundException("invalid principal!");
        }

        // 添加用户的权限
        final List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        principal.getRoles().stream().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.toString())));

        return new org.springframework.security.core.userdetails.User(username, principal.getPassword(), authorities);
    }
}