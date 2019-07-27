package com.andlvovsky.periodicals.model.publication;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PublicationMapper {

    Publication fromDto(PublicationDto publicationDto);

    PublicationDto toDto(Publication publication);

}
