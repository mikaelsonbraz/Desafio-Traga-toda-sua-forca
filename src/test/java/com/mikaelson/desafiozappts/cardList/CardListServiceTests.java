package com.mikaelson.desafiozappts.cardList;

import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.models.entites.CardList;
import com.mikaelson.desafiozappts.api.models.repositories.CardListRepository;
import com.mikaelson.desafiozappts.api.models.repositories.CardRepository;
import com.mikaelson.desafiozappts.api.services.CardListService;
import com.mikaelson.desafiozappts.api.services.implementations.CardListServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CardListServiceTests {

    CardListService service;

    @MockBean
    CardListRepository repository;

    @MockBean
    CardRepository cardRepository;

    @MockBean
    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    public void setUp(){
        this.service = new CardListServiceImpl(repository, cardRepository);
    }

    public CardList createCardList(){
        return CardList.builder().idCardList(1).listName("Minha lista").build();
    }

    public Card createCard(){
        return Card.builder()
                .idCard(1)
                .cardName("Bola de Fogo")
                .edition("Commander Legends: Batalha por Portal de Baldur")
                .isFoil(true)
                .language(3)
                .price(10)
                .build();
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
    @DisplayName("Must return all cards of a cardList sorted by name")
    public void getAllCardsByNameTest(){
        //scenery
        CardList cardList = createCardList();
        Card card = createCard();
        Card card2 = Card.builder().idCard(2).cardName("Bola de Água").edition("Trezegue").isFoil(true).price(12).build();
        cardList.setCards(List.of(card, card2));
        BDDMockito.given(repository.findById(Mockito.anyInt())).willReturn(Optional.of(cardList));

        //execution
        List<String> foundCards = service.getAllCardsByName(1);

        //verifications
        Assertions.assertThat(foundCards.size()).isEqualTo(2);
        Assertions.assertThat(foundCards.contains("\n" + card.getCardName() + ", " + card.getEdition() + ", " + card.getPrice())).isTrue();
        Assertions.assertThat(foundCards.contains("\n" + card2.getCardName() + ", " + card2.getEdition() + ", " + card2.getPrice())).isTrue();
    }

    @Test
    @DisplayName("Must return all cards of a cardlist sorted by price")
    public void getAllCardsByPriceTest(){
        //scenery
        CardList cardList = createCardList();
        Card card = createCard();
        Card card2 = Card.builder().idCard(2).cardName("Bola de Água").edition("Trezegue").isFoil(true).price(12).build();
        cardList.setCards(List.of(card, card2));
        BDDMockito.given(repository.findById(Mockito.anyInt())).willReturn(Optional.of(cardList));

        //execution
        List<String> foundCards = service.getAllCardsByPrice(1);

        //verifications
        Assertions.assertThat(foundCards.size()).isEqualTo(2);
        Assertions.assertThat(foundCards.contains("\n" + card.getPrice() + ", " + card.getCardName() + ", " + card.getEdition())).isTrue();
        Assertions.assertThat(foundCards.contains("\n" + card2.getPrice() + ", " + card2.getCardName() + ", " + card2.getEdition())).isTrue();
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
    @DisplayName("Must place a card in the cardList")
    public void placeCardTest(){
        //scenery
        CardList cardList = createCardList();
        Card card = createCard();
        Mockito.when(repository.save(Mockito.any(CardList.class))).thenReturn(cardList);


        //execution
        CardList placedCard = service.placeCard(cardList, card);

        //verifications
        Assertions.assertThat(placedCard.getIdCardList()).isNotNull();
        Assertions.assertThat(placedCard.getCards().contains(card)).isTrue();
    }

    @Test
    @DisplayName("Must throw IllergalArgumentException when not find a card to put in tha cardList")
    public void putInvalidCardOnCardListTest(){
        //scenery
        CardList cardList = createCardList();
        Card card = new Card();

        //execution

        //verifications
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.placeCard(cardList, card));
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

    @Test
    @DisplayName("Must remove a card of the cardList")
    public void removeCardOfTheCardListTest(){
        //scenery
        CardList cardList = createCardList();
        Card card = createCard();
        Card card2 = Card.builder().idCard(2).cardName("Bola de Água").edition("Trezegue").isFoil(true).price(12).build();
        cardList.setCards(List.of(card, card2));
        Mockito.when(repository.save(Mockito.any(CardList.class))).thenReturn(cardList);

        //execution
        CardList removedCard = service.removeCard(cardList, card2);

        //verifications
        Assertions.assertThat(removedCard.getCards().size()).isEqualTo(1);
        Assertions.assertThat(removedCard.getCards().contains(card)).isTrue();
        Assertions.assertThat(removedCard.getCards().contains(card2)).isFalse();
    }
}
