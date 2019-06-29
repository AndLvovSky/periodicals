package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.controller.PublicationController;
import com.andlvovsky.periodicals.controller.PublicationControllerAdvice;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PeriodicalsApplicationTests {

    @Autowired
    private PublicationController publicationController;

    @Autowired
    private PublicationControllerAdvice publicationControllerAdvice;

    @Autowired
    private PublicationRepository publicationRepository;

    @Test
    public void contextLoads() {
        assertNotNull(publicationController);
        assertNotNull(publicationControllerAdvice);
        assertNotNull(publicationRepository);
    }

}
