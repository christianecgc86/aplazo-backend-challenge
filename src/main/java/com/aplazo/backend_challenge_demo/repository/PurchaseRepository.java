package com.aplazo.backend_challenge_demo.repository;

import com.aplazo.backend_challenge_demo.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
