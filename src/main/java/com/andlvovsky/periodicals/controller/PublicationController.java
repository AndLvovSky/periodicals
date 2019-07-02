package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publications")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @GetMapping("/{id}")
    public Publication getOne(@PathVariable Long id) {
        return publicationService.getOne(id);
    }

    @GetMapping("/")
    public List<Publication> getAll() {
        return publicationService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public void add(@RequestBody Publication publication) {
        publicationService.add(publication);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void replace(@PathVariable Long id, @RequestBody Publication newPublication) {
        publicationService.replace(id, newPublication);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        publicationService.delete(id);
    }

}
