package com.mikaelson.desafiozappts.api.models.repositories;

import com.mikaelson.desafiozappts.api.models.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
