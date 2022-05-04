package com.mikaelson.desafiozappts.api.resources;

import com.mikaelson.desafiozappts.api.models.dtos.CardDto;
import com.mikaelson.desafiozappts.api.models.entities.Card;
import com.mikaelson.desafiozappts.api.services.CardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cards")
@Api("Card API")
public class CardController {

    private final CardService service;
    private final ModelMapper modelMapper;

    public CardController(CardService service, ModelMapper modelMapper){
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a Card")
    public CardDto create(@RequestBody CardDto cardDto){
        Card entity = modelMapper.map(cardDto, Card.class);
        entity = service.save(entity);
        return modelMapper.map(entity, CardDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get a Card")
    public CardDto read(@PathVariable Integer id){
        return service.getById(id)
                .map(card -> modelMapper.map(card, CardDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Update aa Card")
    public CardDto update(@PathVariable Integer id, CardDto cardDto){
        return service.getById(id)
                .map(card -> {
                    card.setCardName(cardDto.getCardName());
                    card.setEdition(cardDto.getEdition());
                    card.setLanguage(cardDto.getLanguage());
                    card.setFoil(cardDto.isFoil());
                    card.setPrice(cardDto.getPrice());
                    card = service.update(card);
                    return modelMapper.map(card, CardDto.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete a Card")
    public void delete(@PathVariable Integer id){
        Card card = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(card);
    }
}
