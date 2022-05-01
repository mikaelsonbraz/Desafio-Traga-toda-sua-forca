package com.mikaelson.desafiozappts.api.services.implementations;

import com.mikaelson.desafiozappts.api.models.entites.CardList;
import com.mikaelson.desafiozappts.api.models.repositories.CardListRepository;
import com.mikaelson.desafiozappts.api.services.CardListService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CardListServiceImpl implements CardListService {

    CardListRepository repository;

    public CardListServiceImpl(CardListRepository repository){
        this.repository = repository;
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
    public CardList update(CardList cardList){
        if(Objects.equals(cardList, null) || Objects.equals(cardList.getIdCardList(), null)){
            throw new IllegalArgumentException("CardList Id cannot be null");
        }
        return repository.save(cardList);
    }

    @Override
    public void delete(CardList cardList){
        if(Objects.equals(cardList, null) || Objects.equals(cardList.getIdCardList(), null)){
            throw new IllegalArgumentException("CardList Id cannot be null");
        }
        repository.delete(cardList);
    }
}
