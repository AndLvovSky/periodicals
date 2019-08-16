package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

}
