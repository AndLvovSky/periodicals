package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return repository.findAll();
    }

    @Override
    public void add(Publication publication) {
        repository.save(publication);
    }

    @Override
    public void replace(Long id, Publication newPublication) {
        Optional<Publication> opt = repository.findById(id);
        if (opt.isPresent()) {
            Publication publication = opt.get();
            publication.setName(newPublication.getName());
            publication.setFrequency(newPublication.getFrequency());
            publication.setCost(newPublication.getCost());
            publication.setDescription(newPublication.getDescription());
            repository.save(publication);
        } else {
            newPublication.setId(id);
            repository.save(newPublication);
        }
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
