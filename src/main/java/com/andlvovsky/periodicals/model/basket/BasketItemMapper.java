package com.andlvovsky.periodicals.model.basket;

import com.andlvovsky.periodicals.service.PublicationService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {PublicationService.class}, componentModel = "spring")
public interface BasketItemMapper {

    @Mapping(source = "publicationId", target = "publication")
    BasketItem fromDto(BasketItemDto basketItemDto);

    @Mapping(source = "publication.id", target = "publicationId")
    BasketItemDto toDto(BasketItem basketItem);

}
