package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
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
    public void testFindById() {
        Publication publication = repository.findById(1L).get();
        assertEquals(publication.getName(), "New York Times");
    }

    @Test
    @DataSet("datasets/publications.json")
    public void testFindAll() {
        List<Publication> publications = repository.findAll();
        assertEquals(publications.size(), 2);
        assertThat(publications.get(1).getFrequency()).isEqualTo(30);
    }

    @Test
    @DataSet("datasets/publications.json")
    public void testSave() {
        repository.save(new Publication("The Sun", 1, 5.5, "-"));
        assertEquals(repository.count(), 3);
    }

    @Test
    @DataSet("datasets/publications.json")
    public void testDelete() {
        repository.deleteById(1L);
        assertEquals(repository.count(), 1);
        Publication publication = repository.findById(2L).get();
        assertEquals(publication.getName(), "New Yorker");
    }

}
