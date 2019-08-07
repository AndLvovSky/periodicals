package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.model.user.Privilege;
import com.andlvovsky.periodicals.model.user.Role;
import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = repository.findByName(name).orElseThrow(() -> new UsernameNotFoundException(name));
        return org.springframework.security.core.userdetails.User
                .withUsername(name)
                .password(user.getPassword())
                .authorities(getAllAuthorities(user.getRoles()))
                .build();
    }

    private List<GrantedAuthority> getAllAuthorities(Collection<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPrivileges().stream())
                .map(Privilege::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
