package com.andlvovsky.periodicals.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String password;

    @ManyToMany
    private Collection<Role> roles;

    public User(String name, String password, Collection<Role> roles) {
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

}
