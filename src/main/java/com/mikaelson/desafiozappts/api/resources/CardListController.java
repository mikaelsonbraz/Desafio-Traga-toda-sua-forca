package com.mikaelson.desafiozappts.api.resources;

import com.mikaelson.desafiozappts.api.models.dtos.CardListDto;
import com.mikaelson.desafiozappts.api.models.entites.CardList;
import com.mikaelson.desafiozappts.api.services.CardListService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cardlists")
public class CardListController {

    private final CardListService service;
    private final ModelMapper modelMapper;

    public CardListController(CardListService service, ModelMapper modelMapper){
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardListDto create(@RequestBody CardListDto cardListDto){
        CardList entity = modelMapper.map(cardListDto, CardList.class);
        entity = service.save(entity);
        return modelMapper.map(entity, CardListDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardListDto read(@PathVariable Integer id){
        return service.getById(id)
                .map(cardList -> modelMapper.map(cardList, CardListDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardListDto update(@PathVariable Integer id, CardListDto cardListDto){
        return service.getById(id)
                .map(cardList -> {
                    cardList.setListName(cardListDto.getListName());
                    cardList = service.update(cardList);
                    return modelMapper.map(cardList, CardListDto.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        CardList cardList = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(cardList);
    }
}
