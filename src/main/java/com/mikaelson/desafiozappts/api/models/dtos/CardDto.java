package com.mikaelson.desafiozappts.api.models.dtos;

import com.mikaelson.desafiozappts.api.models.enums.Language;
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

    private String cardName;

    private String edition;

    private Integer language;

    private boolean isFoil;

    private double price;

    public Language getLanguage(){
        if (language != null){
            return Language.toEnum(language);
        }
        return null;
    }

    public void setLanguage(Language code){
        if (code != null){
            this.language = code.getCode();
        }
    }

    public String getLanguageDescription(){
        if (language != null){
            return this.getLanguage().getDescription();
        }
        return null;
    }

}
