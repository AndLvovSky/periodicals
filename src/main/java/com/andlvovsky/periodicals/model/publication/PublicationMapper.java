package com.andlvovsky.periodicals.model.publication;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PublicationMapper {

    PublicationMapper INSTANCE = Mappers.getMapper(PublicationMapper.class);

    Publication fromDto(PublicationDto publicationDto);

    PublicationDto toDto(Publication publication);

}
