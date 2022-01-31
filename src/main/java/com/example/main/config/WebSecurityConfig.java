package com.example.main.config;

import com.example.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http    .headers(headers -> headers.frameOptions(frameOptions -> frameOptions
                .sameOrigin()))
                .authorizeRequests()
                    .antMatchers("/","/login","/registration","/get_csrf_api").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/videos/**").permitAll()
                    .anyRequest().authenticated()
                .and().csrf()
                .and().formLogin()
                    .loginPage("/login").failureUrl("/login?error=true")
                    .defaultSuccessUrl("/logged")
                .and().rememberMe().key("uniqueAndSecret")
                .and()
                    .logout().deleteCookies("JSESSIONID")
                    .permitAll();
    }
}
