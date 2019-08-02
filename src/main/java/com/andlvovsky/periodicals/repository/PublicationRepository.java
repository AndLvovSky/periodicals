package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.publication.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    List<Publication> findAllByOrderById();

}
