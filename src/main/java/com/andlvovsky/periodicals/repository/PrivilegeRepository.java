package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.user.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

}
