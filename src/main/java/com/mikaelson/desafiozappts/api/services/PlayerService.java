package com.mikaelson.desafiozappts.api.services;

import com.mikaelson.desafiozappts.api.models.entities.Player;

import java.util.Optional;

public interface PlayerService {

    Player save(Player player);

    Optional<Player> getById(Integer id);

    Player update(Player player);

    void delete(Player player);

}
