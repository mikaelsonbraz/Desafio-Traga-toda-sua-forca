package com.mikaelson.desafiozappts.api.resources;

import com.mikaelson.desafiozappts.api.models.dtos.CardListDto;
import com.mikaelson.desafiozappts.api.models.entites.Card;
import com.mikaelson.desafiozappts.api.models.entites.CardList;
import com.mikaelson.desafiozappts.api.services.CardListService;
import com.mikaelson.desafiozappts.api.services.CardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cardlists")
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

    @PatchMapping("/idCardList={idCardList}/idCard={idCard}")
    @ResponseStatus(HttpStatus.OK)
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
    public void delete(@PathVariable Integer id){
        CardList cardList = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(cardList);
    }
}
