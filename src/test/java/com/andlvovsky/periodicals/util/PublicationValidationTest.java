package com.andlvovsky.periodicals.util;

import com.andlvovsky.periodicals.dto.PublicationDto;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class PublicationValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void nameIsTooShort() {
        PublicationDto publication = new PublicationDto(1L, "A", 7, new BigDecimal("10"), "-");
        Set<ConstraintViolation<PublicationDto>> constraintViolations = validator.validate(publication);
        assertEquals(1, constraintViolations.size());
        assertEquals("A", constraintViolations.iterator().next().getInvalidValue());
    }

    @Test
    public void periodMustBePositive() {
        PublicationDto publication = new PublicationDto(2L, "The Guardian", 0, new BigDecimal("15.5"), "-");
        Set<ConstraintViolation<PublicationDto>> constraintViolations = validator.validate(publication);
        assertEquals(1, constraintViolations.size());
        assertEquals(0, constraintViolations.iterator().next().getInvalidValue());
    }

    @Test
    public void costMustNotBeNegative() {
        PublicationDto publication = new PublicationDto(1L, "The Sun", 30, new BigDecimal("-5"), "-");
        Set<ConstraintViolation<PublicationDto>> constraintViolations = validator.validate(publication);
        assertEquals(1, constraintViolations.size());
        assertEquals(new BigDecimal("-5"), constraintViolations.iterator().next().getInvalidValue());
    }

    @Test
    public void publicationIsValid() {
        PublicationDto publication = new PublicationDto(
                1L, "The New York Times", 14, new BigDecimal("20.25"), null);
        Set<ConstraintViolation<PublicationDto>> constraintViolations = validator.validate(publication);
        assertEquals(0, constraintViolations.size());
    }

}
