package com.mikaelson.desafiozappts.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikaelson.desafiozappts.api.models.dtos.PlayerDto;
import com.mikaelson.desafiozappts.api.models.entites.Player;
import com.mikaelson.desafiozappts.api.resources.PlayerController;
import com.mikaelson.desafiozappts.api.services.PlayerService;
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
@WebMvcTest(controllers = PlayerController.class)
@AutoConfigureMockMvc
public class PlayerControllerTests {

    static String PLAYER_API = "/api/players/";

    @Autowired
    MockMvc mvc;

    @MockBean
    PlayerService service;

    public PlayerDto createPlayerDto(){
        return PlayerDto.builder().name("João").build();
    }

    public Player createPlayer(){
        return Player.builder().idPlayer(1).name("João").build();
    }

    @Test
    @DisplayName("Must create a player")
    public void createPlayerTest() throws Exception{
        //scenery
        PlayerDto playerDto = createPlayerDto();
        Player player = createPlayer();
        BDDMockito.given(service.save(Mockito.any(Player.class))).willReturn(player);
        String json = new ObjectMapper().writeValueAsString(playerDto);

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PLAYER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("idPlayer").value(player.getIdPlayer()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(player.getName()));
    }

    @Test
    @DisplayName("Must get a player by id")
    public void getPlayerByIdTest() throws Exception{
        //scenery
        Player player = createPlayer();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(player));

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PLAYER_API.concat(String.valueOf(1)))
                .accept(MediaType.APPLICATION_JSON);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idPlayer").value(player.getIdPlayer()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(player.getName()));
    }

    @Test
    @DisplayName("Must throw ResponseStatusException when cannot find a player by id")
    public void getInvalidPlayerByIdTest() throws Exception{
        //scecnery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PLAYER_API.concat(String.valueOf(1)))
                .accept(MediaType.APPLICATION_JSON);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Must update a player by id")
    public void updatePlayerByIdTest() throws Exception{
        //scenery
        String json = new ObjectMapper().writeValueAsString(PlayerDto.builder().idPlayer(1).name("José").build());
        Player player = createPlayer();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(player));
        BDDMockito.given(service.update(Mockito.any(Player.class))).willReturn(Player.builder().idPlayer(1).name("José").build());


        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PLAYER_API.concat("1"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idPlayer").value(player.getIdPlayer()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("José"));
    }

    @Test
    @DisplayName("Must throw ResponseStatusException when cannot find a player by id to update")
    public void updateInvalidPlayerTest() throws Exception{
        //scenery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());
        String json = new ObjectMapper().writeValueAsString(new PlayerDto());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PLAYER_API.concat("1"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Must delete a player")
    public void deletePlayerTest() throws Exception{
        //scenery
        Player player = createPlayer();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(player));

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PLAYER_API.concat("1"));

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Must throw ReponseStatusException when cannot find a player by id to delete")
    public void deleteInvalidPlayerTest() throws Exception{
        //scenery
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PLAYER_API.concat("1"));

        //verifications
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
