package org.zerock.projectmeongmung.repository;

import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.entity.VetRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VetRecommendationRepository extends JpaRepository<VetRecommendation, Long> {

    boolean existsByUserUidAndVetVetid(String uid, Long vetId);

    default void saveRecommendation(Vet vet, String username) {
        VetRecommendation recommendation = VetRecommendation.builder()
                .vet(vet)
                .user(vet.getUser())
                .build();
        save(recommendation);
    }

}
