package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.model.publication.Publication;
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

// don't work with H2 database
// throw org.dbunit.dataset.NoSuchTableException: 
// The table 'publication' does not exist in schema 'null'
// when processing @DataSet
//
// @Test
// public void testSQL() throws SQLException {
//     Connection conn = dataSource.getConnection();
//     System.out.println(conn.getSchema());
//     PreparedStatement ps = conn.prepareStatement("select * from publication");
//     ps.execute();
//     ResultSet rs = ps.getResultSet();
//     System.out.println(rs.getMetaData().getColumnName(2));
// }
// while abovementioned code inside class prints 
// PUBLIC
// COST
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
        assertEquals("New York Times", publication.getName());
    }

    @Test
    @DataSet("datasets/publications.json")
    public void testFindAll() {
        List<Publication> publications = repository.findAll();
        assertEquals(2, publications.size());
        assertThat(publications.get(1).getFrequency()).isEqualTo(30);
    }

    @Test
    @DataSet("datasets/publications.json")
    public void testSave() {
        repository.save(new Publication("The Sun", 1, 5.5, "-"));
        assertEquals(3, repository.count());
    }

    @Test
    @DataSet("datasets/publications.json")
    public void testDelete() {
        repository.deleteById(1L);
        assertEquals(1, repository.count());
        Publication publication = repository.findById(2L).get();
        assertEquals("New Yorker", publication.getName());
    }

}
