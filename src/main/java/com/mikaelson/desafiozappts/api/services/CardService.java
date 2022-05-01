package com.mikaelson.desafiozappts.api.services;

import com.mikaelson.desafiozappts.api.models.entites.Card;

import java.util.Optional;

public interface CardService {

    Card save(Card card);

    Optional<Card> getById(Integer id);

    Card update(Card card);

    void delete(Card card);
}
