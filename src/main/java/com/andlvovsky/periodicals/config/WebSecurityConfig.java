package com.andlvovsky.periodicals.config;

import com.andlvovsky.periodicals.meta.ClientPages;
import com.andlvovsky.periodicals.meta.Endpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/js/**", "/css/**", ClientPages.ERROR).permitAll()
                    .antMatchers(ClientPages.PUBLICATIONS_EDIT).hasAuthority("EDIT_PUBLICATIONS")
                    .antMatchers(HttpMethod.GET, Endpoints.PUBLICATIONS).hasAuthority("READ_PUBLICATIONS")
                    .antMatchers(HttpMethod.GET, Endpoints.PUBLICATIONS + "/*").hasAuthority("READ_PUBLICATIONS")
                    .antMatchers(HttpMethod.POST, Endpoints.PUBLICATIONS).hasAuthority("EDIT_PUBLICATIONS")
                    .antMatchers(HttpMethod.PUT, Endpoints.PUBLICATIONS + "/*").hasAuthority("EDIT_PUBLICATIONS")
                    .antMatchers(HttpMethod.DELETE, Endpoints.PUBLICATIONS + "/*").hasAuthority("EDIT_PUBLICATIONS")
                    .antMatchers(ClientPages.PUBLICATIONS_VIEW, Endpoints.BASKET,
                            Endpoints.BASKET + "/**", ClientPages.BASKET).hasAuthority("READ_PUBLICATIONS")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage(ClientPages.LOGIN)
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .permitAll()
                    .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
