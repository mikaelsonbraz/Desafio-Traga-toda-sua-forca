package com.mikaelson.desafiozappts.api.services;

import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.models.entites.CardList;

import java.util.Optional;

public interface CardListService {

    CardList save(CardList cardList);

    Optional<CardList> getById(Integer id);

    CardList update(CardList cardList);

    void delete(CardList cardList);

    CardList placeCard(CardList cardList, Card card);
}
