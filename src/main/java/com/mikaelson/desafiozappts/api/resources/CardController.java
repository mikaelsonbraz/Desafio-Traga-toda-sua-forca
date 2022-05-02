package com.mikaelson.desafiozappts.api.resources;

import com.mikaelson.desafiozappts.api.models.dtos.CardDto;
import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.services.CardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService service;
    private final ModelMapper modelMapper;

    public CardController(CardService service, ModelMapper modelMapper){
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardDto create(@RequestBody CardDto cardDto){
        Card entity = modelMapper.map(cardDto, Card.class);
        entity = service.save(entity);
        return modelMapper.map(entity, CardDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardDto read(@PathVariable Integer id){
        return service.getById(id)
                .map(card -> modelMapper.map(card, CardDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
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
    public void delete(@PathVariable Integer id){
        Card card = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(card);
    }
}
