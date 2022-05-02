package com.mikaelson.desafiozappts.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikaelson.desafiozappts.api.models.dtos.CardDto;
import com.mikaelson.desafiozappts.api.models.dtos.PlayerDto;
import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.resources.CardController;
import com.mikaelson.desafiozappts.api.services.CardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CardController.class)
@AutoConfigureMockMvc
public class CardControllerTests {

    static String CARD_API = "/api/cards/";

    @Autowired
    MockMvc mvc;

    @MockBean
    CardService service;

    public CardDto createCardDto(){
        return CardDto.builder()
                .cardName("Bola de Fogo")
                .edition("Commander Legends: Batalha por Portal de Baldur")
                .isFoil(true)
                .language(3)
                .price(10)
                .build();
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
    @DisplayName("Must create a card")
    public void createCardTest() throws Exception{
        //scenery
        Card card = createCard();
        CardDto cardDto = createCardDto();
        BDDMockito.given(service.save(Mockito.any(Card.class))).willReturn(card);
        String json = new ObjectMapper().writeValueAsString(cardDto);

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CARD_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("idCard").value(card.getIdCard()))
                .andExpect(MockMvcResultMatchers.jsonPath("cardName").value(card.getCardName()));
    }

    @Test
    @DisplayName("Must get a card by id")
    public void getCardByIdTest() throws Exception{
        //scenery
        Card card = createCard();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(card));

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CARD_API.concat("1"))
                .accept(MediaType.APPLICATION_JSON);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idCard").value(card.getIdCard()))
                .andExpect(MockMvcResultMatchers.jsonPath("cardName").value(card.getCardName()))
                .andExpect(MockMvcResultMatchers.jsonPath("language").value("PORTUGUESE"));
    }

    @Test
    @DisplayName("Must throw ResponseStatusException when cannot find a card by id")
    public void getInvalidCardByIdTest() throws Exception{
        //scenery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CARD_API.concat("1"))
                .accept(MediaType.APPLICATION_JSON);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Must update a card by id")
    public void updateCardByIdTest() throws Exception{
        //scenery
        String json = new ObjectMapper().writeValueAsString(CardDto.builder().cardName("Bola de Água").build());
        Card card = createCard();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(card));
        BDDMockito.given(service.update(Mockito.any(Card.class))).willReturn(Card.builder().idCard(1).cardName("Bola de Água").build());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CARD_API.concat("1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idCard").value(card.getIdCard()))
                .andExpect(MockMvcResultMatchers.jsonPath("cardName").value("Bola de Água"));
    }

    @Test
    @DisplayName("Must throw ResponseStatusException when try to update an invalid card by id")
    public void updateInvalidCardTest() throws Exception{
        //scenery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());
        String json = new ObjectMapper().writeValueAsString(new CardDto());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CARD_API.concat("1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Must delete a card")
    public void deleteCardByIdTest() throws Exception{
        //scenery
        Card card = createCard();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(card));

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CARD_API.concat("1"));

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Must throw ResponseStatusException when try to delete an invalid card by id")
    public void deleteInvalidCardByIdTest() throws Exception{
        //scenery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CARD_API.concat("1"));

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
