package com.mikaelson.desafiozappts.api.services.implementations;

import com.mikaelson.desafiozappts.api.models.entities.Player;
import com.mikaelson.desafiozappts.api.models.repositories.PlayerRepository;
import com.mikaelson.desafiozappts.api.services.PlayerService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    PlayerRepository repository;

    public PlayerServiceImpl(PlayerRepository repository){
        this.repository = repository;
    }

    @Override
    public Player save(Player player){
        return repository.save(player);
    }

    @Override
    public Optional<Player> getById(Integer id){
        return repository.findById(id);
    }

    @Override
    public Player update(Player player){
        if(Objects.equals(player, null) || Objects.equals(player.getIdPlayer(), null)){
            throw new IllegalArgumentException("Player Id cannot be null");
        }
        return repository.save(player);
    }

    @Override
    public void delete(Player player){
        if(Objects.equals(player, null) || Objects.equals(player.getIdPlayer(), null)){
            throw new IllegalArgumentException("Player Id cannot be null");
        }
        repository.delete(player);
    }
}
