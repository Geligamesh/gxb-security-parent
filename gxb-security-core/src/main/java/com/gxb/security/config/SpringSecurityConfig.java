package com.gxb.security.config;

import com.gxb.security.authentication.authorize.AuthorizeConfigureManager;
import com.gxb.security.authentication.code.ImageCodeValidateFilter;
import com.gxb.security.authentication.mobile.MobileAuthenticationConfig;
import com.gxb.security.authentication.mobile.MobileValidateFilter;
import com.gxb.security.authentication.session.CustomLogoutHandler;
import com.gxb.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;
import java.lang.invoke.MethodType;

@Configuration
//开启springsecurity过滤器链
@EnableWebSecurity
@Slf4j
//开启注解方法级别权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private UserDetailsService customUserDetailsService;
    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailHandler;
    @Autowired
    private ImageCodeValidateFilter imageCodeValidateFilter;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MobileValidateFilter mobileValidateFilter;
    @Autowired
    //校验手机号是否存在，就是手机号认证
    private MobileAuthenticationConfig mobileAuthenticationConfig;
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;
    @Autowired
    private AuthorizeConfigureManager authorizeConfigureManager;

    /**
     * 退出清除缓存
     */
    @Autowired
    private CustomLogoutHandler customLogoutHandler;
    /**
     * 当同个用户session数量超过指定值之后，会调用这个实现类
     */
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    /**
     * 记住我功能
     * @return
     */
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //是否启动项目时自动创建表
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //明文+随机盐值 加密存储
        //$2a$10$ESMvAKcaH4sdFwZhXGCEWOqc9TslCgahjzM.YjuptvqRE.6RjzBAm
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     * 1.认证信息（用户名密码）
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //数据库存储的密码是必须要加密后的
        //不然会报错 There is no PasswordEncoder mapped for the id "null"
        // String password = passwordEncoder().encode("admin");
        // log.info("存储加密之后的密码:{}", password);
        //账号密码信息存储在内存中
        // auth.inMemoryAuthentication()
        //         .withUser("admin")
        //         .password(password)
        //         .authorities("ADMIN");

        auth.userDetailsService(customUserDetailsService);
    }

    /**
     * 当你认证成功之后，SpringSecurity它就会重定向到你上一次的请求中
     * 资源权限配置：
     * 1. 被拦截的资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.httpBasic()//使用httpBasic方式
        http.addFilterBefore(mobileValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin() //登录表单方式
                .loginPage(securityProperties.getAuthentication().getLoginPage())
                .loginProcessingUrl(securityProperties.getAuthentication().getLoginProcessingUrl()) // 登录表单提交处理url，默认是url
                .usernameParameter(securityProperties.getAuthentication().getUsernameParameter())//默认是username
                .passwordParameter(securityProperties.getAuthentication().getPasswordParameter())//默认是password
                .successHandler(customAuthenticationSuccessHandler) //认证成功处理器
                .failureHandler(customAuthenticationFailHandler) //认证失败处理器
                // .and()
                // .authorizeRequests()//认证请求
                // .antMatchers(securityProperties.getAuthentication().getLoginPage(),
                //         securityProperties.getAuthentication().getMobileCodeUrl(),
                //         securityProperties.getAuthentication().getMobilePage(),
                //         securityProperties.getAuthentication().getImageCodeUrl())
                // .permitAll() // 放行login/page不需要认证可访问
                // .antMatchers("/user").hasAuthority("sys:user")
                // .antMatchers(HttpMethod.GET,"/role").hasAuthority("sys:role")
                // .antMatchers(HttpMethod.GET,"/permission").access("hasAuthority('sys:permission') or hasAnyRole('ADMIN','ROOT')")
                // // .antMatchers("/user").hasRole("ADMIN")
                // // .antMatchers("/user").hasAnyRole("ADMIN","ROOT")
                // .anyRequest().authenticated()//所有访问该应用的http请求都要通过身份认证才  可以访问
                .and()
                .rememberMe() // 记住功能配置
                .tokenRepository(jdbcTokenRepository()) //保存登录信息
                .tokenValiditySeconds( securityProperties.getAuthentication().getTokenValiditySeconds()) //记住我有效时长
                .and()
                .sessionManagement() // session管理
                .invalidSessionStrategy(invalidSessionStrategy) // 当session失效后的处理类
                .maximumSessions(1) // 每个用户只能有一个session
                .expiredSessionStrategy(sessionInformationExpiredStrategy) //超过最大数执行实现类
                // .maxSessionsPreventsLogin(true) //当一个用户达到最大session数，则不允许后面再登录
                .sessionRegistry(sessionRegistry())
                .and().and().logout()
                .addLogoutHandler(customLogoutHandler) //退出清除缓存
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/mobile/page")
                .deleteCookies("JSESSIONID")
                ;
        //关闭跨站请求伪造
        http.csrf().disable();
        http.apply(mobileAuthenticationConfig);
        //将所有的授权配置管理起来
        authorizeConfigureManager.config(http.authorizeRequests());
    }

    /**
     * 为了解决退出重新登录的问题
     * @return
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 一般针对静态资源放行
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(securityProperties.getAuthentication().getStaticPaths());
    }


}
