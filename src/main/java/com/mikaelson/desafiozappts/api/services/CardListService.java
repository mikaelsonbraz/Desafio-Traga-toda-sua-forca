package com.mikaelson.desafiozappts.api.services;

import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.models.entites.CardList;

import java.util.List;
import java.util.Optional;

public interface CardListService {

    CardList save(CardList cardList);

    Optional<CardList> getById(Integer id);

    List<String> getAllCardsByName(Integer id);

    List<String> getAllCardsByPrice(Integer id);

    CardList update(CardList cardList);

    CardList placeCard(CardList cardList, Card card);

    void delete(CardList cardList);

    CardList removeCard(CardList cardList, Card card);


}
