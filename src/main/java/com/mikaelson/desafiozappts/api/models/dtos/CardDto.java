package com.mikaelson.desafiozappts.api.models.dtos;

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
public class CardDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCard;

    @NotNull
    private String cardName;

    @NotNull
    private String edition;

    @NotNull
    private Integer language;

    @NotNull
    private boolean isFoil;

    @NotNull
    private double price;

}
