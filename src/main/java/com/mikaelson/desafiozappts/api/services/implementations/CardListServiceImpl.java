package com.mikaelson.desafiozappts.api.services.implementations;

import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.models.entites.CardList;
import com.mikaelson.desafiozappts.api.models.repositories.CardListRepository;
import com.mikaelson.desafiozappts.api.models.repositories.CardRepository;
import com.mikaelson.desafiozappts.api.services.CardListService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CardListServiceImpl implements CardListService {

    CardListRepository repository;

    CardRepository cardRepository;

    public CardListServiceImpl(CardListRepository repository, CardRepository cardRepository){
        this.repository = repository;
        this.cardRepository = cardRepository;
    }

    @Override
    public CardList save(CardList cardList){
        return repository.save(cardList);
    }

    @Override
    public Optional<CardList> getById(Integer id){
        return repository.findById(id);
    }

    @Override
    public List<String> getAllCardsByName(Integer id){
        List<Card> cards = repository.findById(id).get().getCards();
        List<String> allCards = new ArrayList<>();
        for(Card card : cards){
            allCards.add("\n" + card.getCardName() + ", " + card.getEdition() + ", " + card.getPrice());
        }
        Collections.sort(allCards);
        return allCards;
    }

    @Override
    public List<String> getAllCardsByPrice(Integer id){
        List<Card> cards = repository.findById(id).get().getCards();
        List<String> allCards = new ArrayList<>();
        for(Card card : cards){
            allCards.add("\n" + card.getPrice()  + ", " + card.getCardName() + ", " + card.getEdition());
        }
        Collections.sort(allCards);
        return allCards;
    }

    @Override
    public CardList update(CardList cardList){
        if(Objects.equals(cardList, null) || Objects.equals(cardList.getIdCardList(), null)){
            throw new IllegalArgumentException("CardList Id cannot be null");
        }
        return repository.save(cardList);
    }

    @Override
    public CardList placeCard(CardList cardList, Card card){
        if(Objects.equals(cardList, null) || Objects.equals(cardList.getIdCardList(), null)
        || Objects.equals(card, null) || Objects.equals(card.getIdCard(), null)){
            throw new IllegalArgumentException("CardList Id or Card Id cannot be null");
        }
        if(Objects.equals(cardList.getCards(), null)){
            cardList.setCards(List.of(card));
            card.setCardList(cardList);
            cardRepository.save(card);
            return repository.save(cardList);
        }
        cardList.getCards().add(card);
        card.setCardList(cardList);
        cardRepository.save(card);
        return repository.save(cardList);
    }

    @Override
    public void delete(CardList cardList){
        if(Objects.equals(cardList, null) || Objects.equals(cardList.getIdCardList(), null)){
            throw new IllegalArgumentException("CardList Id cannot be null");
        }
        repository.delete(cardList);
    }

    @Override
    public CardList removeCard(CardList cardList, Card card){
        if(Objects.equals(cardList, null) || Objects.equals(cardList.getIdCardList(), null)
                || Objects.equals(card, null) || Objects.equals(card.getIdCard(), null)){
            throw new IllegalArgumentException("CardList Id or Card Id cannot be null");
        }
        List<Card> cards = new ArrayList<>();
        for(Card card1 : cardList.getCards()){
            if(!Objects.equals(card1, card)){
                cards.add(card1);
            }
            card1.setCardList(null);
            cardRepository.save(card1);
        }
        cardList.setCards(cards);
        return repository.save(cardList);
    }
}
