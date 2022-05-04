package com.mikaelson.desafiozappts.cardList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikaelson.desafiozappts.api.models.dtos.CardListDto;
import com.mikaelson.desafiozappts.api.models.dtos.PlayerDto;
import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.models.entites.CardList;
import com.mikaelson.desafiozappts.api.resources.CardListController;
import com.mikaelson.desafiozappts.api.services.CardListService;
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
@WebMvcTest(controllers = CardListController.class)
@AutoConfigureMockMvc
public class CardListControllerTests {

    static String CARDLIST_API = "/api/cardlists/";

    @Autowired
    MockMvc mvc;

    @MockBean
    CardListService service;

    @MockBean
    CardService cardService;

    public CardListDto createCardListDto(){
        return CardListDto.builder().idCardList(1).listName("Minha Lista").build();
    }

    public CardList createCardList(){
        return CardList.builder().idCardList(1).listName("Minha Lista").build();
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
    @DisplayName("Must create a cardList")
    public void createCardListTest() throws Exception{
        //scennery
        CardList cardList = createCardList();
        CardListDto cardListDto = createCardListDto();
        BDDMockito.given(service.save(Mockito.any(CardList.class))).willReturn(cardList);
        String json = new ObjectMapper().writeValueAsString(cardListDto);

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CARDLIST_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        ///verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("idCardList").value(cardList.getIdCardList()))
                .andExpect(MockMvcResultMatchers.jsonPath("listName").value(cardList.getListName()));
    }

    @Test
    @DisplayName("Must get a cardlist by id")
    public void getCardListByIdTest() throws Exception{
        //scenery
        CardList cardList = createCardList();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(cardList));

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CARDLIST_API.concat("1"))
                .accept(MediaType.APPLICATION_JSON);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idCardList").value(cardList.getIdCardList()))
                .andExpect(MockMvcResultMatchers.jsonPath("listName").value(cardList.getListName()));
    }

    @Test
    @DisplayName("Must throw ResponseStatusExcecption when cannot find a cardList by id")
    public void getInvalidCardListByIdTest() throws Exception {
        //scenery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CARDLIST_API.concat("1"))
                .accept(MediaType.APPLICATION_JSON);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Must update a cardList by id")
    public void updateCardListTest() throws Exception {
        //scenery
        String json = new ObjectMapper().writeValueAsString(CardListDto.builder().idCardList(1).listName("Nova Lista").build());
        CardList cardList = createCardList();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(cardList));
        BDDMockito.given(service.update(Mockito.any(CardList.class))).willReturn(CardList.builder().idCardList(1).listName("Nova Lista").build());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CARDLIST_API.concat("1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idCardList").value(cardList.getIdCardList()))
                .andExpect(MockMvcResultMatchers.jsonPath("listName").value("Nova Lista"));
    }

    @Test
    @DisplayName("Must throw ResponseStatusExceeption when try to update an invalid cardList by id")
    public void updateInvalidCardListByIdTest() throws Exception{
        ///scenery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());
        String json = new ObjectMapper().writeValueAsString(new CardListDto());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CARDLIST_API.concat("1"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        ///verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Must save a card on a cardList")
    public void putCardOnCardListTest() throws Exception{
        //scenery
        CardList cardList = createCardList();
        Card card = createCard();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(cardList)).willReturn(Optional.of(cardList));
        BDDMockito.given(cardService.getById(Mockito.anyInt())).willReturn(Optional.of(card)).willReturn(Optional.of(card));
        BDDMockito.given(service.placeCard(Mockito.any(CardList.class), Mockito.any(Card.class))).willReturn(cardList);

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch(CARDLIST_API.concat("idCardList=1/idCard=1"))
                .accept(MediaType.APPLICATION_JSON);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idCardList").value(cardList.getIdCardList()));
    }

    @Test
    @DisplayName("Must throw ResponseStatusException when not find a cadList or card to update the cardlist with the card")
    public void putInvalidCardOnCardListTest() throws Exception{
        //scenery
        CardList cardList = createCardList();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(cardList));
        BDDMockito.given(cardService.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch(CARDLIST_API.concat("idCardList=1/idCard=1"))
                .accept(MediaType.APPLICATION_JSON);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @DisplayName("Must delete a cardList")
    public void deleteCardListByIdTest() throws Exception{
        //scenery
        CardList cardList = createCardList();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(cardList));

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CARDLIST_API.concat("1"));

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Must throw ResponseStatusException when try to delete an invalid cardList by id")
    public void deleteInvalidCardListByIdTest() throws Exception{
        //scenery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CARDLIST_API.concat("1"));

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
