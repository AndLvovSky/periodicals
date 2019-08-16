package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.dto.PublicationDto;

import java.util.List;

public interface PublicationService {

    PublicationDto getOne(Long id);

    List<PublicationDto> getAll();

    void add(PublicationDto publication);

    // if publication with specified id does not exist, creates a new entity
    void replace(Long id, PublicationDto newPublication);

    void delete(Long id);

}
