package com.andlvovsky.periodicals.model.basket;

import org.mapstruct.Mapper;

@Mapper(uses = {BasketItemMapper.class}, componentModel = "spring")
public interface BasketMapper {

    Basket fromDto(BasketDto basketDto);

    BasketDto toDto(Basket basket);

}
