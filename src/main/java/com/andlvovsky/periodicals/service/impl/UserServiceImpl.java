package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.UserRepository;
import com.andlvovsky.periodicals.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return repository.findByName(username);
    }

}
