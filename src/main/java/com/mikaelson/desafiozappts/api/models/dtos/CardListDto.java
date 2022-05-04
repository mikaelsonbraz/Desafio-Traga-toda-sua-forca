package com.mikaelson.desafiozappts.api.models.dtos;

import com.mikaelson.desafiozappts.api.models.entites.Player;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardListDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCardList;

    @NotNull
    private String listName;

    private PlayerDto listCardsOwner;
}
