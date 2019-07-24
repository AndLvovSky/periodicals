package com.andlvovsky.periodicals.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy="roles")
    private Collection<User> users;

    @ManyToMany
    private Collection<Privilege> privileges;

    public Role(String name, Collection<Privilege> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

}
