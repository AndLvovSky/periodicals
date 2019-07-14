package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.publication.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

}
