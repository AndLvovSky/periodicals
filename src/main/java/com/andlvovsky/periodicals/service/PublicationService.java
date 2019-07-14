package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.publication.Publication;

import java.util.List;

public interface PublicationService {

    Publication getOne(Long id);

    List<Publication> getAll();

    void add(Publication publication);

    void replace(Long id, Publication newPublication);

    void delete(Long id);

}
