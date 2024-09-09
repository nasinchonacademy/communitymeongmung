package org.zerock.projectmeongmung.repository;

import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.entity.VetRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetRecommendationRepository extends JpaRepository<VetRecommendation, Long> {
    VetRecommendation findByVetAndUser(Vet vet, User user);

    boolean existsByUserAndVet(User user, Vet vet);
}
