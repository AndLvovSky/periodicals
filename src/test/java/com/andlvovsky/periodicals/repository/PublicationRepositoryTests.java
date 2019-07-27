package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.publication.Publication;
import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PublicationRepositoryTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    DataSource dataSource;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> dataSource.getConnection());

    @Autowired
    private PublicationRepository repository;

    @Test
    @DataSet("datasets/publications.json")
    public void findsById() {
        Publication publication = repository.findById(101L).get();
        assertEquals("New York Times", publication.getName());
    }

    @Test
    @DataSet("datasets/publications.json")
    public void findsAll() {
        List<Publication> publications = repository.findAll();
        assertEquals(2, publications.size());
        assertThat(publications.get(1).getPeriod()).isEqualTo(30);
    }

    @Test
    @DataSet("datasets/publications.json")
    public void saves() {
        repository.save(new Publication("The Sun", 1, 5.5, "-"));
        assertEquals(3, repository.count());
    }

    @Test
    @DataSet("datasets/publications.json")
    public void deletes() {
        repository.deleteById(101L);
        assertEquals(1, repository.count());
        Publication publication = repository.findById(102L).get();
        assertEquals("New Yorker", publication.getName());
    }

}
