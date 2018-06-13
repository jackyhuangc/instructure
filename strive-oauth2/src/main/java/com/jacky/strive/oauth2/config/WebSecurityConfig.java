package com.jacky.strive.oauth2.config;

import com.jacky.strive.common.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 配置用户安全认证信息，以及受保护路径、允许访问路径
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    /**
     * 配置.忽略的静态文件，不加的话，登录之前页面的css,js不能正常使用，得登录之后才能正常.
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略URL
        web.ignoring().antMatchers("/**/*.js", "/lang/*.json", "/**/*.css", "/**/*.js", "/**/*.map", "/**/*.html",
                "/**/*.png");

        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * 注意，以下几项一定要放于权限范围内
         *
         * 1、/login,否则无法sso正常跳转 2、/logout,否则无在sso服务器上进行法注销
         * 3、/home,自定义首页，方便在页面上放置注销按钮 4、/oauth/authorize,授权访问地址，否则会报User must be
         * authenticated with Spring Security before authorization can be
         * completed.
         *
         * 备注：1和2的目的是为了保证在sso服务器页面上能正常的进行登录和注销
         *
         * 这样，我们便可以在任何的sso客户端，分两步清除token缓存，1、清除本地cookie，2、跳转到服务器sso服务器页面，
         * 可选择是否进行注销，注销后，相关sso子系统都无法再使用
         */
        http.requestMatchers().antMatchers("/", "/home", "/doLogin", "/logout", "/oauth/authorize").and()
                .authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/login")
                // 表单请求的路由为/doLogin,默认为/login,此处有意区分
                .loginProcessingUrl("/doLogin")
                .defaultSuccessUrl("/").failureUrl("/login?error=1").permitAll().and().logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=1").permitAll().invalidateHttpSession(true).clearAuthentication(true);

        http.csrf().disable();
    }

    /**
     * 重写用户信息加载服务
     *
     * @return
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new CustomUserService();
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // 内存用户验证
        // auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");

        // 自定义验证方式一：重写AuthenticationProvider(内部调用UserDetailsService)
        //auth.authenticationProvider(new SsoAuthenticationProvider());

        // 自定义验证方式二：直接重写UserDetailsService验证
        PasswordEncoder passwordEncoder = new PasswordEncoder() {
            // 使用MD5获取加密之后的密码
            @Override
            public String encode(CharSequence rawPassword) {
                return Md5Util.encode((String) rawPassword);
            }

            // 验证密码
            @Override
            public boolean matches(CharSequence rawPassword, String
                    encodedPassword) {
                if (encodedPassword.equals(rawPassword) || encodedPassword.equals(encode(rawPassword))) {
                    return true;
                }

                logger.info("invalid password!");
                throw new BadCredentialsException("invalid password!");
            }
        };
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        /*
         * Consider defining a bean of type 'org.springframework.security.authentication.AuthenticationManager' in your configuration.
         * 解决authenticationmanager无法注入的问题
         */
        return super.authenticationManagerBean();
    }

    @Bean(name = BeanIds.USER_DETAILS_SERVICE)
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
        //return new CustomUserService();
    }
}
