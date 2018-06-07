package com.jacky.strive.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 配置用户安全认证信息，以及受保护路径、允许访问路径
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // 内存用户验证
        // auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");

        // 自定义SSO身份认证
        auth.authenticationProvider(new SsoAuthenticationProvider());

        // // user Details Service验证
        // auth.userDetailsService(customUserService()).passwordEncoder(new
        // PasswordEncoder() {
        // // 使用MD5获取加密之后的密码
        // @Override
        // public String encode(CharSequence rawPassword) {
        // return Md5Util.encode((String) rawPassword);
        // }
        //
        // // 验证密码
        // @Override
        // public boolean matches(CharSequence rawPassword, String
        // encodedPassword) {
        // return encodedPassword.equals(Md5Util.encode((String) rawPassword));
        // }
        // });
    }
}
