package com.mikaelson.desafiozappts.api.models.repositories;

import com.mikaelson.desafiozappts.api.models.entities.CardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardListRepository extends JpaRepository<CardList, Integer> {
}
