package com.mikaelson.desafiozappts.api.resources;

import com.mikaelson.desafiozappts.api.models.dtos.PlayerDto;
import com.mikaelson.desafiozappts.api.models.entites.Player;
import com.mikaelson.desafiozappts.api.services.PlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/players")
public class PlayerController {

    private final PlayerService service;
    private final ModelMapper modelMapper;

    public PlayerController(PlayerService service, ModelMapper modelMapper){
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDto create(@RequestBody PlayerDto playerDto){
        Player entity = modelMapper.map(playerDto, Player.class);
        entity = service.save(entity);
        return modelMapper.map(entity, PlayerDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerDto read(@PathVariable Integer id){
        return service.getById(id)
                .map(player -> modelMapper.map(player, PlayerDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerDto update(@PathVariable Integer id, PlayerDto playerDto){
        return service.getById(id)
                .map(player -> {
                    player.setName(playerDto.getName());
                    player = service.update(player);
                    return modelMapper.map(player, PlayerDto.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        Player player = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(player);
    }
}
