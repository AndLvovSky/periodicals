package com.andlvovsky.periodicals.mapper;

import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BasketItemMapperTests {

    @InjectMocks
    private BasketItemMapperImpl mapper;

    @Mock
    private PublicationRepository publicationRepository;

    private Publication publication = new Publication(1L,"The Guardian", 7, 10., "-");

    private BasketItem basketItem = new BasketItem(publication, 5);

    private BasketItemDto basketItemDto = new BasketItemDto(1L, 5);

    @Before
    public void beforeEach() {
        when(publicationRepository.findById(1L)).thenReturn(Optional.ofNullable(publication));
    }

    @Test
    public void mapsDtoToEntity() {
        BasketItem actualBasketItem = mapper.fromDto(basketItemDto);
        assertEquals(basketItem, actualBasketItem);
    }

    @Test
    public void mapsEntityToDto() {
        BasketItemDto actualBasketItemDto = mapper.toDto(basketItem);
        assertEquals(basketItemDto, actualBasketItemDto);
    }

}
