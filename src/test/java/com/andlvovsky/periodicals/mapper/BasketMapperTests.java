package com.andlvovsky.periodicals.mapper;

import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.basket.BasketDto;
import com.andlvovsky.periodicals.model.basket.BasketMapperImpl;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.service.impl.PublicationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BasketMapperTests {

    @InjectMocks
    private BasketMapperImpl mapper;

    @Mock
    private PublicationServiceImpl publicationService;

    private Publication publication = new Publication(1L,"The Guardian", 7, 10., "-");

    @Before
    public void beforeEach() {
        when(publicationService.getOne(1L)).thenReturn(publication);
    }

    @Test
    public void mapsDtoToEntity() {
        BasketDto basketDto = new BasketDto(1L, 5);
        Basket basket = mapper.fromDto(basketDto);
        assertEquals(new Basket(publication, 5), basket);
    }

    @Test
    public void mapsEntityToDto() {
        Basket basket = new Basket(publication, 5);
        BasketDto basketDto = mapper.toDto(basket);
        assertEquals(new BasketDto(1L, 5), basketDto);
    }

}
