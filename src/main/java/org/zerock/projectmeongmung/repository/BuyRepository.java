package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.Buy;
import org.zerock.projectmeongmung.entity.User;

import java.util.List;

public interface BuyRepository extends JpaRepository<Buy, Long> {
    List<Buy> findByUser(User user);
}
