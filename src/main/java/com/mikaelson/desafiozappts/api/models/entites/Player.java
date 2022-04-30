package com.mikaelson.desafiozappts.api.models.entites;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer idPlayer;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "listCardsOwner")
    private List<CardList> cardLists;

    @OneToMany(mappedBy = "cardOwner")
    private List<Card> cards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return idPlayer.equals(player.idPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlayer);
    }
}
