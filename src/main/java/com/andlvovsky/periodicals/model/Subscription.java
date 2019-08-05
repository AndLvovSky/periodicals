package com.andlvovsky.periodicals.model;

import com.andlvovsky.periodicals.model.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscription_generator")
    @SequenceGenerator(name = "subscription_generator", sequenceName = "subscription_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Publication publication;

    @ManyToOne
    private User owner;

    private Integer number;

    public Subscription(Publication publication, User owner, Integer number) {
        this.publication = publication;
        this.owner = owner;
        this.number = number;
    }

}
