package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.dto.PublicationDto;
import com.andlvovsky.periodicals.mapper.PublicationMapper;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import com.andlvovsky.periodicals.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository repository;

    private PublicationMapper mapper = Mappers.getMapper(PublicationMapper.class);

    @Override
    public PublicationDto getOne(Long id) {
        Publication publication = repository.findById(id)
                .orElseThrow(() -> new PublicationNotFoundException(id));
        return mapper.toDto(publication);
    }

    @Override
    public List<PublicationDto> getAll() {
        List<Publication> publications = repository.findAll(Sort.by("id"));
        return publications.stream()
                .map(publication -> mapper.toDto(publication))
                .collect(Collectors.toList());
    }

    @Override
    public void add(PublicationDto publicationDto) {
        Publication publication = mapper.fromDto(publicationDto);
        repository.save(publication);
    }

    @Override
    public void replace(Long id, PublicationDto newPublicationDto) {
        Publication newPublication = mapper.fromDto(newPublicationDto);
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
