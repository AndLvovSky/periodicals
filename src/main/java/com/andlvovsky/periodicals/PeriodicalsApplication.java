package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PeriodicalsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeriodicalsApplication.class, args);
    }

    @Bean
    public CommandLineRunner createDefaultRepository(PublicationRepository repository) {
        return (args) -> {
            repository.save(new Publication("Tampa Bay Times", 7, 5f, "-"));
            repository.save(new Publication("Minneapolis Star Tribune", 30, 7.5f, "-"));
            repository.save(new Publication("Philadelphia Inquirer", 7, 10f, "-"));
            repository.save(new Publication("St. Louis Post-Dispatch", 7, 3.75f, "-"));
            repository.save(new Publication("New Jersey Star-Ledger", 14, 11f, "-"));
            repository.save(new Publication("Arizona Republic", 21, 16.2f, "-"));
        };
    }

}
