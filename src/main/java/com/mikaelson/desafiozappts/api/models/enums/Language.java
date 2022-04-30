package com.mikaelson.desafiozappts.api.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Language {

    ENGLISH(1, "English"),
    JAPANESE(2, "Japanese"),
    PORTUGUESE(3, "Portuguese");

    private final Integer code;
    private final String description;

    public static Language toEnum(Integer code){
        if (code == null){
            return null;
        }
        for (Language x : Language.values()){
            if(code.equals(x.getCode())){
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
