package com.jacky.strive.oauth2.controller;

import com.jacky.strive.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

/**
 * 需支持跨域,否则客户端虽能正常访问，但也会报跨域异常
 */
@CrossOrigin()
@RestController
public class TokenController {

    @Autowired
    OauthService oauthService;

    /**
     * 返回自定义的用户身份信息
     *
     * @param user 内置用户身份
     * @return 自定义的用户身份信息
     */
    @RequestMapping({"/user", "/me"})
    public Object user(Principal user) {
        return oauthService.findByUserName(user.getName()).getData();
    }

    @RequestMapping({"/github/token"})
    public String GetGitHubToken(String clientId, String clientSecret, String code) {
        String path = "https://github.com/login/oauth/access_token";

        path += "?client_id=" + clientId;
        path += "&client_secret=" + clientSecret;
        path += "&code=" + code;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(path, String.class);

        return responseEntity.getBody();
    }

    @RequestMapping({"/proxy"})
    public String Proxy(String path) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(path, String.class);

        return responseEntity.getBody();
    }
}