package com.mikaelson.desafiozappts.api.models.entites;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer idCardList;

    @Column(nullable = true)
    private String listName;

    @Column(nullable = false)
    private Integer cardsAmount;

    @OneToMany(mappedBy = "cardList")
    private List<Card> cards;

    @ManyToOne
    @JoinColumn(name = "id_player")
    private Player listCardsOwner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardList cardList = (CardList) o;
        return idCardList.equals(cardList.idCardList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCardList);
    }
}
