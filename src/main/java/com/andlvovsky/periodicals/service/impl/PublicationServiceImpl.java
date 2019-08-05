package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.model.publication.PublicationDto;
import com.andlvovsky.periodicals.model.publication.PublicationMapper;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import com.andlvovsky.periodicals.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository repository;

    private PublicationMapper mapper = Mappers.getMapper(PublicationMapper.class);

    public PublicationDto getOne(Long id) {
        Publication publication = repository.findById(id)
                .orElseThrow(() -> new PublicationNotFoundException(id));
        return mapper.toDto(publication);
    }

    public List<PublicationDto> getAll() {
        List<Publication> publications = repository.findAllByOrderById();
        List<PublicationDto> publicationDtos = new ArrayList<>();
        for (Publication publication : publications) {
            publicationDtos.add(mapper.toDto(publication));
        }
        return publicationDtos;
    }

    public void add(PublicationDto publicationDto) {
        Publication publication = mapper.fromDto(publicationDto);
        repository.save(publication);
    }

    public void replace(Long id, PublicationDto newPublicationDto) {
        Publication newPublication = mapper.fromDto(newPublicationDto);
        newPublication.setId(id);
        repository.save(newPublication);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new PublicationNotFoundException(id);
        }
    }

}
