package com.mikaelson.desafiozappts.api.services.implementations;

import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.models.repositories.CardRepository;
import com.mikaelson.desafiozappts.api.services.CardService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    CardRepository repository;

    public CardServiceImpl(CardRepository repository){
        this.repository = repository;
    }

    @Override
    public Card save(Card card){
        return repository.save(card);
    }

    @Override
    public Optional<Card> getById(Integer id){
        return repository.findById(id);
    }

    @Override
    public Card update(Card card){
        if(Objects.equals(card, null) || Objects.equals(card.getIdCard(), null)){
            throw new IllegalArgumentException("Card Id cannot be null");
        }
        return repository.save(card);
    }

    @Override
    public void delete(Card card){
        if(Objects.equals(card, null) || Objects.equals(card.getIdCard(), null)){
            throw new IllegalArgumentException("Card Id cannot be null");
        }
        repository.delete(card);
    }
}
