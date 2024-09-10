package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.entity.Vet;

import java.util.Optional;

public interface VetRepository extends JpaRepository<Vet, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.vetinfo = null WHERE u.vetinfo.vetid = :vetId")
    void detachVetFromUser(@Param("vetId") Long vetId);
}