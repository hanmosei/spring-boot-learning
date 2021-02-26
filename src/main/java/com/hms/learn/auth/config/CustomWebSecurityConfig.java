package com.hms.learn.auth.config;

import com.hms.learn.auth.handler.CustomAuthenticationFailureHandler;
import com.hms.learn.auth.handler.CustomAuthenticationSuccessHandler;
import com.hms.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 自定义Security配置
 *
 * @author chentay
 * @date 2021-02-26 12:42
 */
@Configuration
public class CustomWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    //  角色继承
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_DBA > ROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CustomFilterInvocationSecurityMetadataSource cfisms() {
        return new CustomFilterInvocationSecurityMetadataSource();
    }
    @Bean
    CustomAccessDecisionManager cadm() {
        return new CustomAccessDecisionManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("root").password("$2a$10$6l3K54uUe5IfsQWXML0wgOG9ncTeFfXpD3P9t5ICK0NnG5KxRvR7q").roles("ADMIN", "DBA")
//                .and()
//                .withUser("admin").password("$2a$10$6l3K54uUe5IfsQWXML0wgOG9ncTeFfXpD3P9t5ICK0NnG5KxRvR7q").roles("ADMIN", "USER")
//                .and()
//                .withUser("hanmosi").password("$2a$10$6l3K54uUe5IfsQWXML0wgOG9ncTeFfXpD3P9t5ICK0NnG5KxRvR7q").roles("USER");
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/**/admin/**")
//                .hasAnyRole("ADMIN")
//                .antMatchers("/**/user/**")
//                .access("hasAnyRole('ADMIN','USER')")
//                .antMatchers("/**/db/**")
//                .access("hasAnyRole('ADMIN') and hasAnyRole('DBA')")
//                .anyRequest()
//                .authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(cfisms());
                        object.setAccessDecisionManager(cadm());
                        return object;
                    }
                })
                .and()
                .formLogin()
//                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .failureHandler(new CustomAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .addLogoutHandler((request, response, authentication) -> {

                })
                .logoutSuccessHandler((request, response, authentication) -> response.sendRedirect("/login"))
                .permitAll()
                .and()
                .csrf().disable();
    }
}
