package com.andlvovsky.periodicals.config;

import com.andlvovsky.periodicals.model.user.Privilege;
import com.andlvovsky.periodicals.model.user.Role;
import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.PrivilegeRepository;
import com.andlvovsky.periodicals.repository.RoleRepository;
import com.andlvovsky.periodicals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDataLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void run(ApplicationArguments args) {
        deleteAll();
        createPrivilege("READ_PUBLICATIONS");
        createPrivilege("EDIT_PUBLICATIONS");
        createRole("ADMIN", "EDIT_PUBLICATIONS");
        createRole("USER", "READ_PUBLICATIONS");
        createUser("a", "p", "USER", "ADMIN");
        createUser("u", "p", "USER");
    }

    private void deleteAll() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        privilegeRepository.deleteAll();
    }

    private void createPrivilege(String name) {
        Privilege privilege = new Privilege(name);
        privilegeRepository.save(privilege);
    }

    private void createRole(String roleName, String... privilegeNames) {
        List<Privilege> rolePrivileges = new ArrayList<>();
        for (String privilegeName : privilegeNames) {
            Privilege privilege = privilegeRepository.findByName(privilegeName);
            rolePrivileges.add(privilege);
        }
        Role role = new Role("ROLE_" + roleName, rolePrivileges);
        roleRepository.save(role);
    }

    private void createUser(String userName, String password, String... roleNames) {
        List<Role> userRoles = new ArrayList<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName("ROLE_" + roleName);
            userRoles.add(role);
        }
        User user = new User(userName, passwordEncoder.encode(password), userRoles);
        userRepository.save(user);
    }

}
