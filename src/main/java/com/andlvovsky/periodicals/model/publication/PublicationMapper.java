package com.andlvovsky.periodicals.model.publication;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PublicationMapper {

    public static final PublicationMapper INSTANCE = Mappers.getMapper(PublicationMapper.class);

    public abstract Publication fromDto(PublicationDto publicationDto);

    public abstract PublicationDto toDto(Publication publication);

}
