package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.Vet;

public interface VetRepository extends JpaRepository<Vet, Long> {
}