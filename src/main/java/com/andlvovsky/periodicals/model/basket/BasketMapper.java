package com.andlvovsky.periodicals.model.basket;

import com.andlvovsky.periodicals.service.impl.PublicationServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {PublicationServiceImpl.class}, componentModel = "spring")
public interface BasketMapper {

    @Mapping(source = "publicationId", target = "publication")
    Basket fromDto(BasketDto basketDto);

    @Mapping(source = "publication.id", target = "publicationId")
    BasketDto toDto(Basket basket);

}
