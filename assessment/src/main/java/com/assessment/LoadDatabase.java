package com.assessment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BookRepository book_repository, BorrowerRepository borrower_repository) {

        return args -> {
            log.info("Preloading " + book_repository.save(new Book("Foundation", "Isaac Asimov")));
            log.info("Preloading " + book_repository.save(new Book("Alloy of Law", "Brandon Sanderson")));
            log.info("Preloading " + book_repository.save(new Book("Sir Callie and the Champions of Helston", "Esme Symes-Smith")));
            log.info("Preloading " + book_repository.save(new Book("I Want My Hat Back", "Jon Klassen")));

            log.info("Preloading " + borrower_repository.save(new Borrower("Edeleth von Hresvelg")));
            log.info("Preloading " + borrower_repository.save(new Borrower("Kirigaya Kazuto")));
            log.info("Preloading " + borrower_repository.save(new Borrower("Amina al-Sirafi")));

        };
    }
}