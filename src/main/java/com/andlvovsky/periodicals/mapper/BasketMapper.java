package com.andlvovsky.periodicals.mapper;

import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.dto.BasketDto;
import org.mapstruct.Mapper;

@Mapper(uses = {BasketItemMapper.class}, componentModel = "spring")
public interface BasketMapper {

    Basket fromDto(BasketDto basketDto);

    BasketDto toDto(Basket basket);

}
