package com.andlvovsky.periodicals.mapper;

import com.andlvovsky.periodicals.dto.BasketItemDto;
import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BasketItemMapperTests {

    @InjectMocks
    private BasketItemMapperImpl mapper;

    @Mock
    private PublicationRepository publicationRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Publication publication = new Publication(1L,"The Guardian", 7, new BigDecimal("10"), "-");

    private BasketItem basketItem = new BasketItem(publication, 5);

    private BasketItemDto basketItemDto = new BasketItemDto(1L, 5);

    @Before
    public void beforeEach() {
        when(publicationRepository.findById(1L)).thenReturn(Optional.ofNullable(publication));
        doThrow(new PublicationNotFoundException(99L)).when(publicationRepository).findById(99L);
    }

    @Test
    public void mapsDtoToEntity() {
        BasketItem actualBasketItem = mapper.fromDto(basketItemDto);
        assertEquals(basketItem, actualBasketItem);
    }

    @Test
    public void mapDtoToEntityFailsNotExistingPublication() {
        expectedException.expect(PublicationNotFoundException.class);
        expectedException.expectMessage(containsString("99"));
        BasketItem actualBasketItem = mapper.fromDto(new BasketItemDto(99L, 7));
        assertEquals(basketItem, actualBasketItem);
    }

    @Test
    public void mapsEntityToDto() {
        BasketItemDto actualBasketItemDto = mapper.toDto(basketItem);
        assertEquals(basketItemDto, actualBasketItemDto);
    }

}
