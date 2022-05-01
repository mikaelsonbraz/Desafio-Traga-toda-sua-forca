package com.mikaelson.desafiozappts.cardList;

import com.mikaelson.desafiozappts.api.models.entites.CardList;
import com.mikaelson.desafiozappts.api.models.repositories.CardListRepository;
import com.mikaelson.desafiozappts.api.services.CardListService;
import com.mikaelson.desafiozappts.api.services.implementations.CardListServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CardListServiceTests {

    CardListService service;

    @MockBean
    CardListRepository repository;

    @MockBean
    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    public void setUp(){
        this.service = new CardListServiceImpl(repository);
    }

    public CardList createCardList(){
        return CardList.builder().idCardList(1).listName("Minha lista").build();
    }

    @Test
    @DisplayName("Must save a cardList")
    public void saveCardListTest(){
        //scenery
        CardList cardList = createCardList();
        Mockito.when(repository.save(cardList)).thenReturn(cardList);

        //execution
        CardList savedCardList = service.save(cardList);

        //verifications
        Assertions.assertThat(savedCardList.getIdCardList()).isNotNull();
        Assertions.assertThat(savedCardList.getListName()).isEqualTo(cardList.getListName());
    }

    @Test
    @DisplayName("Must obtain a cardList by id")
    public void getCardListByIdTest(){
        //scenery
        CardList cardList = createCardList();
        Mockito.when(repository.findById(cardList.getIdCardList())).thenReturn(Optional.of(cardList));

        //execution
        Optional<CardList> foundCardList = service.getById(1);

        //verifications
        Assertions.assertThat(foundCardList.isPresent()).isTrue();
        Assertions.assertThat(foundCardList.get().getIdCardList()).isNotNull();
        Assertions.assertThat(foundCardList.get().getListName()).isEqualTo(cardList.getListName());
    }

    @Test
    @DisplayName("Must return Optional.empty() when try to find a non existent cardList on db")
    public void cardListNotFoundByIdTest(){
        //scenery
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        //execution
        Optional<CardList> foundCardList = service.getById(1);

        //verifications
        Assertions.assertThat(foundCardList.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Must update a cardList")
    public void updateCardListTest(){
        //scenery
        CardList cardList = createCardList();
        Mockito.when(repository.save(cardList)).thenReturn(cardList);

        //execution
        CardList updatedCardList = service.update(cardList);

        //verifications
        Assertions.assertThat(updatedCardList.getIdCardList()).isNotNull();
        Assertions.assertThat(updatedCardList.getListName()).isEqualTo(cardList.getListName());
    }

    @Test
    @DisplayName("Must throw IllegalArgumentException when try to update an invalid cardList")
    public void updateInvalidCardListTest(){
        //scenery
        CardList cardList = new CardList();

        //execution

        //verifications
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(cardList));
        Mockito.verify(repository, Mockito.never()).save(cardList);
    }

    @Test
    @DisplayName("Must delete a cardList")
    public void deleteCardListTest(){
        //scenery
        CardList cardList = createCardList();
        entityManager.persist(cardList);

        //execution
        service.delete(cardList);
        CardList deletedCardList = entityManager.find(CardList.class, cardList);

        //verifications
        Mockito.verify(repository, Mockito.times(1)).delete(cardList);
        Assertions.assertThat(deletedCardList).isNull();
    }

    @Test
    @DisplayName("Must throw IllegalArgumentException whe try to delete an invalid cardList")
    public void deleteInvalidCardListTest(){
        //scenery
        CardList cardList = new CardList();

        //execution

        //verifications
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(cardList));
        Mockito.verify(repository, Mockito.never()).delete(cardList);
    }
}
