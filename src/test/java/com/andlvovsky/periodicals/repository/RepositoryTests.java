package com.andlvovsky.periodicals.repository;

import com.github.database.rider.core.DBUnitRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public class RepositoryTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    DataSource dataSource;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> dataSource.getConnection());

}
