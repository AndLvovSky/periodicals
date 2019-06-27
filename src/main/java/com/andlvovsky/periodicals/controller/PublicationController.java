package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.repository.PublicationSimpleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/publications")
public class PublicationController {

    @ResponseBody
    @GetMapping("/{id}")
    public Publication getPublication(@PathVariable int id) {
        return PublicationSimpleRepository.publications.get(id);
    }

}
