package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.meta.Endpoints;
import com.andlvovsky.periodicals.model.publication.PublicationDto;
import com.andlvovsky.periodicals.model.publication.PublicationMapper;
import com.andlvovsky.periodicals.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(Endpoints.PUBLICATIONS)
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;

    @GetMapping("/{id}")
    public PublicationDto getOne(@PathVariable Long id) {
        return publicationService.getOne(id);
    }

    @GetMapping("")
    public List<PublicationDto> getAll() {
        return publicationService.getAll();
    }

    @PostMapping("")
    public ResponseEntity<List<ObjectError>> add(
            @Valid @RequestBody PublicationDto publicationDto, Errors validationResult) {
        if (validationResult.hasErrors()) {
            return new ResponseEntity<>(validationResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        publicationService.add(publicationDto);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<List<ObjectError>> replace(
            @PathVariable Long id, @Valid @RequestBody PublicationDto newPublicationDto, Errors validationResult) {
        if (validationResult.hasErrors()) {
            return new ResponseEntity<>(validationResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        publicationService.replace(id, newPublicationDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        publicationService.delete(id);
    }

}
