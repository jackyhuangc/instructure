package com.jacky.strive.oauth2.controller;

import com.jacky.strive.oauth2.domain.User;
import com.jacky.strive.oauth2.domain.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.security.Principal;

@CrossOrigin() // 需支持跨域,否则客户端虽能正常访问，但也会报跨域异常
@RestController
public class TokenController {

	/**
	 * 返回自定义的用户身份信息
	 *
	 * @param user
	 *            内置用户身份
	 * 
	 *
	 * @return 自定义的用户身份信息
	 */
	@RequestMapping({ "/user", "/me" })
	public User user(Principal user) {
		User investor = null;

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

			investor = userMapper.getByUserName(user.getName());
			//investor.setName(user.getName());

		} catch (Exception e) {
		}

		return investor;
	}

	// @RequestMapping({ "/user", "/me" })
	// public Principal user(Principal user) {
	// return user;
	// }

	@RequestMapping({ "/github/token" })
	public String GetGitHubToken(String clientId, String clientSecret, String code) {
		String path = "https://github.com/login/oauth/access_token";

		path += "?client_id=" + clientId;
		path += "&client_secret=" + clientSecret;
		path += "&code=" + code;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(path, String.class);

		return responseEntity.getBody();
	}

	@RequestMapping({ "/proxy" })
	public String Proxy(String path) {

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(path, String.class);

		return responseEntity.getBody();
	}
}