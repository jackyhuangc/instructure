package com.jacky.strive.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

// 如果启动会变成resource server ，会覆盖掉websecurityconfig配置
// 如果取消，则会使用websecurityconfig配置，变成client sso
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {

        /*
         * ant风格路径表达式规则 Wilcard Description * Matches zero or more characters.
         * ?Matches exactly one character. ** Matches zero or more
         * directories(**可匹配文件夹及子文件夹下的路径).
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and().requestMatchers()// 允许制定request调用httpsecurity
                .antMatchers("/**").and()
                // 需要单独的将各种method请求设置跨域响应，而不能简单的使用permitAll
                .authorizeRequests().antMatchers(HttpMethod.GET, "/getInstrument").permitAll() // 先设置不需要权限的
                .antMatchers(HttpMethod.GET, "/upload/**").permitAll()// 这是图片资源路径
                .antMatchers(HttpMethod.POST, "/upload").permitAll()
                .antMatchers(HttpMethod.POST, "/upload/batch").permitAll()
                .antMatchers(HttpMethod.GET, "/**").authenticated() // 再设置需要权限的
                .antMatchers(HttpMethod.POST, "/**").authenticated();
    }
}