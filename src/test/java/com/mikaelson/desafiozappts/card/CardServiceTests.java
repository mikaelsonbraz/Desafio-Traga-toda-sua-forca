package com.mikaelson.desafiozappts.card;

import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.models.repositories.CardRepository;
import com.mikaelson.desafiozappts.api.services.CardService;
import com.mikaelson.desafiozappts.api.services.implementations.CardServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CardServiceTests {

    CardService service;

    @MockBean
    CardRepository repository;

    @MockBean
    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    public void setUp(){
        this.service = new CardServiceImpl(repository);
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
    @DisplayName("Must save a card")
    public void saveCardTest(){
        //scenery
        Card card = createCard();
        Mockito.when(repository.save(card)).thenReturn(card);

        //execution
        Card savedCard = service.save(card);

        //verifications
        Assertions.assertThat(savedCard.getIdCard()).isNotNull();
        Assertions.assertThat(savedCard.getCardName()).isEqualTo(card.getCardName());
        Assertions.assertThat(savedCard.getLanguage().getDescription()).isEqualTo("Portuguese");
        Assertions.assertThat(savedCard.getEdition()).isEqualTo(card.getEdition());
        Assertions.assertThat(savedCard.isFoil()).isTrue();
        Assertions.assertThat(savedCard.getPrice()).isEqualTo(card.getPrice());
    }

    @Test
    @DisplayName("Must obatin a card data by id")
    public void getCardByIdTest(){
        //scenery
        Card card = createCard();
        Mockito.when(repository.findById(card.getIdCard())).thenReturn(Optional.of(card));

        //execution
        Optional<Card> foundCard = service.getById(1);

        //verifications
        Assertions.assertThat(foundCard.isPresent()).isTrue();
        Assertions.assertThat(foundCard.get().getIdCard()).isNotNull();
        Assertions.assertThat(foundCard.get().getCardName()).isEqualTo(card.getCardName());
    }

    @Test
    @DisplayName("Must return Optional.empty() when try to obtain a non existent card on db")
    public void cardNotFoundByIdTest(){
        //scenery
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        //execution
        Optional<Card> foundCard = service.getById(1);

        //verifications
        Assertions.assertThat(foundCard.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Must update a card")
    public void updateCardTest(){
        //scenery
        Card card = createCard();
        Mockito.when(repository.save(card)).thenReturn(card);

        //execution
        Card updatedCard = service.update(card);

        //verifications
        Assertions.assertThat(updatedCard.getIdCard()).isNotNull();
        Assertions.assertThat(updatedCard.getCardName()).isEqualTo(card.getCardName());
        Assertions.assertThat(updatedCard.getLanguage().getDescription()).isEqualTo("Portuguese");
        Assertions.assertThat(updatedCard.getEdition()).isEqualTo(card.getEdition());
    }

    @Test
    @DisplayName("Must throw IllegalArgumentException when try to update an invalid card")
    public void updateInvalidCardTest(){
        //scenery
        Card card = new Card();

        //execution

        //verifications
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(card));
        Mockito.verify(repository, Mockito.never()).save(card);
    }

    @Test
    @DisplayName("Must delete a card")
    public void deleteCardTest(){
        //scenery
        Card card = createCard();
        entityManager.persist(card);

        //execution
        service.delete(card);
        Card deletedCard = entityManager.find(Card.class, card);

        //verifications
        Mockito.verify(repository, Mockito.times(1)).delete(card);
        Assertions.assertThat(deletedCard).isNull();
    }

    @Test
    @DisplayName("Must throw IllegalArgumentException when try to delete a invalid card")
    public void deleteInvalidCardTest(){
        //scenery
        Card card = new Card();

        //execution

        //verifications
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(card));
        Mockito.verify(repository, Mockito.never()).delete(card);
    }
}
