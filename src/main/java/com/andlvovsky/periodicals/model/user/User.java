package com.andlvovsky.periodicals.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="user_entity")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="user_generator")
    @SequenceGenerator(name="user_generator", sequenceName="user_sequence", allocationSize=1)
    private Long id;

    private String name;

    private String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
