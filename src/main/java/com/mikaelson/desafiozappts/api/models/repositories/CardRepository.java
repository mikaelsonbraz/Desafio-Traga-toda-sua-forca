package com.mikaelson.desafiozappts.api.models.repositories;

import com.mikaelson.desafiozappts.api.models.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
