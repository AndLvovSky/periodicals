package com.andlvovsky.periodicals.model.basket;

import org.mapstruct.Mapper;

@Mapper(uses = {BasketItemMapper.class})
public interface BasketMapper {

    Basket fromDto(BasketDto basketDto);

    BasketDto toDto(Basket basket);

}
