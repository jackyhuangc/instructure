package com.jacky.strive.oauth2.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * managing access tokens用于管理令牌服务
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Oauth2DataSourceConfig oauth2DataSourceConfig;

    @Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(oauth2DataSourceConfig.getUrl());
        dataSource.setUsername(oauth2DataSourceConfig.getUsername());
        dataSource.setPassword(oauth2DataSourceConfig.getPassword());
        return dataSource;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(getDataSource());
    }

    @Primary
    @Bean
    public AuthorizationServerTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        final DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(false);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        // 30天
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));

        return tokenServices;
    }

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(getDataSource());
    }

    /**
     * Configure the non-security features of the Authorization Server endpoints
     * 配置授权服务器端点的非安全特性，如token生成规则及存储方式
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);

        // 配置TokenServices参数
        // DefaultTokenServices tokenServices = new DefaultTokenServices();
        // tokenServices.setTokenStore(endpoints.getTokenStore());
        // tokenServices.setSupportRefreshToken(false);
        // tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        // tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        // tokenServices.setAccessTokenValiditySeconds((int)
        // 30天
        // TimeUnit.DAYS.toSeconds(30));

        endpoints.tokenServices(tokenServices(endpoints));
    }

    /**
     * Configure the security of the Authorization Server
     * 配置授权服务器的安全性，如哪些方法可放行/授权
     *
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        //oauthServer.allowFormAuthenticationForClients();
    }

    /**
     * Configure the ClientDetailsService, 配置客户端授权信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // clients.inMemory() // 使用in-memory存储
        // .withClient("client") // client_id
        // .secret("secret") // client_secret
        // .authorizedGrantTypes("authorization_code") // 该client允许的授权类型
        // .scopes("app"); // 允许的授权范围

        // 使用jdbc存储授权信息
        clients.withClientDetails(clientDetails());
    }

    @Configuration
    @EnableResourceServer
    public class ResourceServer extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
            http.csrf().disable();
        }
    }
}
