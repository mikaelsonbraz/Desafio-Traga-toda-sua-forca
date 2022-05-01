package com.mikaelson.desafiozappts.player;

import com.mikaelson.desafiozappts.api.models.entites.Player;
import com.mikaelson.desafiozappts.api.models.repositories.PlayerRepository;
import com.mikaelson.desafiozappts.api.services.PlayerService;
import com.mikaelson.desafiozappts.api.services.implementations.PlayerServiceImpl;
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
public class PlayerServiceTests {

    PlayerService service;

    @MockBean
    PlayerRepository repository;

    @MockBean
    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    public void setUp(){
        this.service = new PlayerServiceImpl(repository);
    }

    public Player createPlayer(){
        return Player.builder().idPlayer(1).name("João").build();
    }

    @Test
    @DisplayName("Must save a player")
    public void savePlayerTest(){
        //scenery
        Player player = createPlayer();
        Mockito.when(repository.save(player)).thenReturn(player);

        //execution
        Player savedPlayer = service.save(player);

        //verifications
        Assertions.assertThat(savedPlayer.getIdPlayer()).isNotNull();
        Assertions.assertThat(savedPlayer.getName()).isEqualTo(player.getName());
    }

    @Test
    @DisplayName("Must obtain a player data by id")
    public void getPlayerByIdTest(){
        //scenery
        Player player = createPlayer();
        Mockito.when(repository.findById(player.getIdPlayer())).thenReturn(Optional.of(player));

        //execution
        Optional<Player> foundPlayer = service.getById(1);

        //verifications
        Assertions.assertThat(foundPlayer.isPresent()).isTrue();
        Assertions.assertThat(foundPlayer.get().getIdPlayer()).isEqualTo(1);
        Assertions.assertThat(foundPlayer.get().getName()).isEqualTo(player.getName());
    }

    @Test
    @DisplayName("Must return Optional.empty() when try to obtain a non existent player by id")
    public void playerNotFoundByIdTest(){
        //scenery
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        //execution
        Optional<Player> foundPlayer = service.getById(1);

        //verifications
        Assertions.assertThat(foundPlayer.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Must update a player on database")
    public void updatePlayerTest(){
        //scenery
        Player player = createPlayer();
        Mockito.when(repository.save(Mockito.any(Player.class))).thenReturn(player);

        //execution
        Player updatedPlayer = service.update(player);

        //verifications
        Assertions.assertThat(updatedPlayer.getIdPlayer()).isNotNull();
        Assertions.assertThat(updatedPlayer.getName()).isEqualTo(player.getName());
    }

    @Test
    @DisplayName("Must throw IllegalArgumentException when try to update a non valid player on database")
    public void updateInvalidPlayerTest(){
        //scenery
        Player player = new Player();

        //execution

        //verifications
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(player));
        Mockito.verify(repository, Mockito.never()).save(player);
    }

    @Test
    @DisplayName("Must delete a player on database")
    public void deletePlayerTest(){
        //scenery
        Player player = createPlayer();
        entityManager.persist(player);

        //execution
        service.delete(player);
        Player deletedPlayer = entityManager.find(Player.class, player);

        //verifications
        Mockito.verify(repository, Mockito.times(1)).delete(player);
        Assertions.assertThat(deletedPlayer).isNull();
    }

    @Test
    @DisplayName("Must throw IllegalArgumentException when try to delete a non existent player o database")
    public void deleteInvalidPlayerTest(){
        //scenery
        Player player = new Player();

        //execution

        //verifications
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(player));
        Mockito.verify(repository, Mockito.never()).delete(player);
    }
}
