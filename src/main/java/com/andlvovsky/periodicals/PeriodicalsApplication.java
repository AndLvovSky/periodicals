package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class PeriodicalsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeriodicalsApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner createDefaultRepository(PublicationRepository repository) {
        return args -> {
            repository.deleteAll();
            repository.save(new Publication("Tampa Bay Times", 7, 5., "-"));
            repository.save(new Publication("Minneapolis Star Tribune", 30, 7.5, "-"));
            repository.save(new Publication("Philadelphia Inquirer", 7, 10., "-"));
            repository.save(new Publication("St. Louis Post-Dispatch", 7, 3.75, "-"));
            repository.save(new Publication("New Jersey Star-Ledger", 14, 11., "-"));
            repository.save(new Publication("Arizona Republic", 21, 16.2, "-"));
        };
    }

}
