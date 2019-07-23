package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    public UserDetails loadUserByUsername(String name) {
        User user = repository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(name)
                .password(user.getPassword())
                .roles("ADMIN")
                .build();
    }

}
