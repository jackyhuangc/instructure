package com.jacky.strive.oauth2.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class SsoAuthenticationProvider implements AuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(SsoAuthenticationProvider.class);

	/**
	 * 注册UserDetailsService 的bean
	 *
	 * @return Core interface which loads user-specific data
	 */
	@Bean
	UserDetailsService customUserService() {
		return new CustomUserService();
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		// 根据token找到用户信息
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		UserDetails userDetails = customUserService().loadUserByUsername(token.getName());
		// 密码MD5加密验证
//		if (!userDetails.getPassword().equals(Md5Util.encode(token.getCredentials().toString()))) {
//			logger.info("invalid password!");
//			throw new BadCredentialsException("invalid password!");
//		}

		// 返回一个Token对象表示登录
		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}
}
