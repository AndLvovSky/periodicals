package com.andlvovsky.periodicals.model.publication;

import org.mapstruct.Mapper;

@Mapper
public interface PublicationMapper {

    Publication fromDto(PublicationDto publicationDto);

    PublicationDto toDto(Publication publication);

}
