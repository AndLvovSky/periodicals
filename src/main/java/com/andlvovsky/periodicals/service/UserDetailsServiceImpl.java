package com.andlvovsky.periodicals.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetails loadUserByUsername(String s) {
        String username = "u";
        if (!username.equals(s)) throw new UsernameNotFoundException(s);
        return User.withDefaultPasswordEncoder()
                .username(username)
                .password("p")
                .roles("ADMIN")
                .build();
    }

}
