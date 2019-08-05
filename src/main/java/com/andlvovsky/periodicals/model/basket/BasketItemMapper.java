package com.andlvovsky.periodicals.model.basket;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BasketItemMapper {

    @Autowired
    private PublicationRepository publicationRepository;

    @Mapping(source = "publicationId", target = "publication")
    public abstract BasketItem fromDto(BasketItemDto basketItemDto);

    @Mapping(source = "publication.id", target = "publicationId")
    public abstract BasketItemDto toDto(BasketItem basketItem);

    public Publication publicationIdToPublication(Long publicationId) {
        return publicationRepository.findById(publicationId)
                .orElseThrow(() -> new PublicationNotFoundException(publicationId));
    }

}
