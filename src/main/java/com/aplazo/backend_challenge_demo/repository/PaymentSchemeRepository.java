package com.aplazo.backend_challenge_demo.repository;

import com.aplazo.backend_challenge_demo.model.PaymentScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentSchemeRepository extends JpaRepository<PaymentScheme, Long> {

    Optional<PaymentScheme> findByName(String name);

}
