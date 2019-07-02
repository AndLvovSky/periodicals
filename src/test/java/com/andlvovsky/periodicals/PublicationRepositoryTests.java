package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class PublicationRepositoryTests {

    @Autowired
    private PublicationRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Publication[] publications = {
        new Publication("New York Times", 7, 15., "-"),
        new Publication("New Yorker", 30, 10., "-")
    };

    @Test
    public void testFindOne() {
        entityManager.persist(publications[0]);
        entityManager.flush();
        Publication publication = repository.findById(publications[0].getId()).get();
        assertEquals(publication.getName(), "New York Times");
    }

    @Test
    public void testFindAll() {
        entityManager.persist(publications[0]);
        entityManager.persist(publications[1]);
        List<Publication> publications = repository.findAll();
        assertEquals(publications.size(), 2);
        assertThat(publications.get(1).getFrequency()).isEqualTo(30);
    }

    @Test
    public void testSave() {
        repository.save(publications[0]);
        entityManager.flush();
        Publication publication = repository.findById(publications[0].getId()).get();
        assertEquals(publication.getCost(), 15, 0.0);
    }

    @Test
    public void testDelete() {
        entityManager.persist(publications[0]);
        entityManager.persist(publications[1]);
        entityManager.flush();
        repository.deleteById(publications[0].getId());
        assertEquals(repository.count(), 1);
        Publication publication = repository.findById(publications[1].getId()).get();
        assertEquals(publication.getName(), "New Yorker");
    }

}
