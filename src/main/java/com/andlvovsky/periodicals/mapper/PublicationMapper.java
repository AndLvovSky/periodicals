package com.andlvovsky.periodicals.mapper;

import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.dto.PublicationDto;
import org.mapstruct.Mapper;

@Mapper
public interface PublicationMapper {

    Publication fromDto(PublicationDto publicationDto);

    PublicationDto toDto(Publication publication);

}
