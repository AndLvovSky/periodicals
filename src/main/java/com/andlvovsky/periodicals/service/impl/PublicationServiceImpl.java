package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import com.andlvovsky.periodicals.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationRepository repository;

    @Override
    public Publication getOne(Long id) {
        return repository.findById(id).orElseThrow(() -> new PublicationNotFoundException(id));
    }

    @Override
    public List<Publication> getAll() {
        return repository.findAllByOrderById();
    }

    @Override
    public void add(Publication publication) {
        repository.save(publication);
    }

    @Override
    public void replace(Long id, Publication newPublication) {
        newPublication.setId(id);
        repository.save(newPublication);
    }

    @Override
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new PublicationNotFoundException(id);
        }
    }

}
