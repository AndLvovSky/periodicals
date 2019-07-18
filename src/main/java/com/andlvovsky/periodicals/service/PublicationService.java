package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.publication.Publication;

import java.util.List;

public interface PublicationService {

    Publication getOne(Long id);

    List<Publication> getAll();

    void add(Publication publication);

    // if publication with specified id does not exist, creates a new entity
    void replace(Long id, Publication newPublication);

    void delete(Long id);

}
