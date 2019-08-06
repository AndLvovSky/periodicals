package com.andlvovsky.periodicals.model.user;

import com.andlvovsky.periodicals.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> roles;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Collection<Subscription> subscriptions;

}
