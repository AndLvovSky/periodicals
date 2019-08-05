package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.Subscription;
import com.andlvovsky.periodicals.model.user.User;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests extends RepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Test
    @DataSet("datasets/subscriptions.json")
    public void loadsCorrectSubscriptions() {
        User user = userRepository.findByName("u").get();
        Collection<Subscription> subscriptions = user.getSubscriptions();
        assertEquals(2, subscriptions.size());
        Subscription subscription = subscriptions.iterator().next();
        assertEquals(user, subscription.getOwner());
        assertEquals(publicationRepository.findById(2L).get(), subscription.getPublication());
    }

}
