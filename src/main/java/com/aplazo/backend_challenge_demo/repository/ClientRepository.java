package com.aplazo.backend_challenge_demo.repository;

import com.aplazo.backend_challenge_demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByFirstNameAndLastNameAndBirthdate(String firstName, String lastName, Date birthdate);

}
