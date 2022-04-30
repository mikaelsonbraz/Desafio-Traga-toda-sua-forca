package com.mikaelson.desafiozappts.api.models.entites;

import com.mikaelson.desafiozappts.api.models.enums.Language;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer idCard;

    @Column(nullable = false)
    private String cardName;

    @Column(nullable = false)
    private String edition;

    @Column(nullable = false)
    private Integer language;

    @Column(nullable = false)
    private boolean isFoil;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private Integer amountSameCard;

    @ManyToOne
    @JoinColumn(name = "id_cardList")
    private CardList cardList;

    @ManyToOne
    @JoinColumn(name = "id_player")
    private Player cardOwner;

    public Language getLanguage(){
        return Language.toEnum(language);
    }

    public void setLanguage(Language code){
        this.language = code.getCode();
    }

    public String getLanguageDescription(){
        return this.getLanguage().getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return idCard.equals(card.idCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCard);
    }
}
