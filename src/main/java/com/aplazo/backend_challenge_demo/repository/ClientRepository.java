package com.aplazo.backend_challenge_demo.repository;

import com.aplazo.backend_challenge_demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
