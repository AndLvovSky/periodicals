package com.andlvovsky.periodicals;

import com.andlvovsky.periodicals.model.publication.PublicationDto;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
        PublicationDto publication = new PublicationDto("A", 7, 10.0, "-");
        Set<ConstraintViolation<PublicationDto>> constraintViolations = validator.validate(publication);
        assertEquals( 1, constraintViolations.size() );
        assertEquals( "A", constraintViolations.iterator().next().getInvalidValue());
    }

    @Test
    public void periodMustBePositive() {
        PublicationDto publication = new PublicationDto("The Guardian", 0, 15.5, "-");
        Set<ConstraintViolation<PublicationDto>> constraintViolations = validator.validate(publication);
        assertEquals( 1, constraintViolations.size() );
        assertEquals( 0, constraintViolations.iterator().next().getInvalidValue());
    }

    @Test
    public void costMustNotBeNegative() {
        PublicationDto publication = new PublicationDto("The Sun", 30, -5.0, "-");
        Set<ConstraintViolation<PublicationDto>> constraintViolations = validator.validate(publication);
        assertEquals( 1, constraintViolations.size() );
        assertEquals( -5.0, constraintViolations.iterator().next().getInvalidValue());
    }

    @Test
    public void publicationIsValid() {
        PublicationDto publication = new PublicationDto(
                "The New York Times", 14, 20.25, null);
        Set<ConstraintViolation<PublicationDto>> constraintViolations = validator.validate(publication);
        assertEquals( 0, constraintViolations.size() );
    }

}
