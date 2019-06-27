package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/publications")
public class PublicationController {

    private final PublicationRepository repository;

    public PublicationController(PublicationRepository repository) {
        this.repository = repository;
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Publication getPublication(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new PublicationNotFoundException(id));
    }

}
