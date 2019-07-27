package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

}
