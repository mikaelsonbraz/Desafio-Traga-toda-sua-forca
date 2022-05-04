package com.mikaelson.desafiozappts.api.resources;

import com.mikaelson.desafiozappts.api.models.dtos.CardListDto;
import com.mikaelson.desafiozappts.api.models.entities.Card;
import com.mikaelson.desafiozappts.api.models.entities.CardList;
import com.mikaelson.desafiozappts.api.services.CardListService;
import com.mikaelson.desafiozappts.api.services.CardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cardlists")
@Api("CardLists API")
public class CardListController {

    private final CardListService service;
    private final CardService cardService;
    private final ModelMapper modelMapper;

    public CardListController(CardListService service, CardService cardService, ModelMapper modelMapper){
        this.service = service;
        this.cardService = cardService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a CardList")
    public CardListDto create(@RequestBody CardListDto cardListDto){
        CardList entity = modelMapper.map(cardListDto, CardList.class);
        entity = service.save(entity);
        return modelMapper.map(entity, CardListDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get a CardList")
    public CardListDto read(@PathVariable Integer id){
        return service.getById(id)
                .map(cardList -> modelMapper.map(cardList, CardListDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/sortby=name/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get Cards from a CardList sorting by name")
    public List<String> readAllCardsByName(@PathVariable Integer id){
        if(service.getById(id).isPresent()){
            return service.getAllCardsByName(id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/sortby=price/{id}")
    @ApiOperation("Get Cards from a CardList sorting by name")
    @ResponseStatus(HttpStatus.OK)
    public List<String> readAllCardsByPrice(@PathVariable Integer id){
        if(service.getById(id).isPresent()){
            return service.getAllCardsByPrice(id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Update a CardList")
    public CardListDto update(@PathVariable Integer id, CardListDto cardListDto){
        return service.getById(id)
                .map(cardList -> {
                    cardList.setListName(cardListDto.getListName());
                    cardList = service.update(cardList);
                    return modelMapper.map(cardList, CardListDto.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/add/idCardList={idCardList}/idCard={idCard}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Add cards on a CardList")
    public CardListDto updateCards(@PathVariable Integer idCardList, @PathVariable Integer idCard){
        if(service.getById(idCardList).isPresent() && cardService.getById(idCard).isPresent()){
            CardList cardList = service.getById(idCardList).get();
            Card card = cardService.getById(idCard).get();
            cardList = service.placeCard(cardList, card);
            return modelMapper.map(cardList, CardListDto.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete a CardList")
    public void delete(@PathVariable Integer id){
        CardList cardList = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(cardList);
    }

    @PatchMapping("/remove/idCardList={idCardList}/idCard={idCard}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Remove Cards from a CardList")
    public CardListDto removeCard(@PathVariable Integer idCardList, @PathVariable Integer idCard){
        if(service.getById(idCardList).isPresent() && cardService.getById(idCard).isPresent()){
            CardList cardList = service.getById(idCardList).get();
            Card card = cardService.getById(idCard).get();
            cardList = service.removeCard(cardList, card);
            return modelMapper.map(cardList, CardListDto.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
