package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publications")
public class PublicationController {

    private final PublicationRepository repository;

    public PublicationController(PublicationRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public Publication getPublication(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new PublicationNotFoundException(id));
    }

    @GetMapping("/")
    public List<Publication> getAllPublications() {
        return repository.findAll();
    }

    @PostMapping("/")
    public Publication addPublication(@RequestBody Publication publication) {
        return repository.save(publication);
    }

    @PutMapping("/{id}")
    public Publication replacePublication(@PathVariable Long id, @RequestBody Publication newPublication) {
        return repository.findById(id)
            .map(publication -> {
                publication.setName(newPublication.getName());
                publication.setFrequency(newPublication.getFrequency());
                publication.setCost(newPublication.getCost());
                publication.setDescription(newPublication.getDescription());
                return repository.save(publication);
            })
            .orElseGet(() -> {
                newPublication.setId(id);
                return repository.save(newPublication);
            });
    }

    @DeleteMapping("/{id}")
    public void deletePublication(@PathVariable Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new PublicationNotFoundException(id);
        }
    }

}
