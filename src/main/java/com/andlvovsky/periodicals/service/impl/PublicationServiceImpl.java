package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.dto.PublicationDto;
import com.andlvovsky.periodicals.mapper.PublicationMapper;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import com.andlvovsky.periodicals.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
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
        List<Publication> publications = repository.findAll(Sort.by("id"));
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
