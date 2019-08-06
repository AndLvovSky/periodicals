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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetails loadUserByUsername(String name) {
        User user = repository.findByName(name).orElseThrow(() -> new UsernameNotFoundException(name));
        return org.springframework.security.core.userdetails.User
                .withUsername(name)
                .password(user.getPassword())
                .authorities(getAllAuthorities(user.getRoles()))
                .build();
    }

    private List<GrantedAuthority> getAllAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.addAll(getRoleAuthorities(role));
        }
        return authorities;
    }

    private List<GrantedAuthority> getRoleAuthorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Privilege privilege : role.getPrivileges()) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }
        return authorities;
    }

}
