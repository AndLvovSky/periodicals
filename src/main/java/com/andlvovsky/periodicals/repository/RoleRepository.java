package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}

