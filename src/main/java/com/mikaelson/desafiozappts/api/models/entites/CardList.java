package com.mikaelson.desafiozappts.api.models.entites;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer idCardList;

    @Column(nullable = true)
    private String listName;

    @OneToMany(mappedBy = "cardList")
    private List<Card> cards = new ArrayList<Card>();

    @ManyToOne
    @JoinColumn(name = "id_player", nullable = false)
    private Player listCardsOwner;

    @Override
    public String toString() {
        return "CardList{" +
                "idCardList=" + idCardList +
                ", listName='" + listName + '\'' +
                ", listCardsOwner=" + listCardsOwner +
                '}';
    }

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
