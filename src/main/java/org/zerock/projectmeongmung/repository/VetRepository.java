package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.Vet;

import java.util.Optional;

public interface VetRepository extends JpaRepository<Vet, Long> {
    Optional<Vet> findByUsername(String username);
    void deleteByUsername(String username);
    Optional<Vet> findByUserId(Long userId);
}