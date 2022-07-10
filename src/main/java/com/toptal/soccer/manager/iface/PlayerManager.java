package com.toptal.soccer.manager.iface;

import com.toptal.soccer.model.Player;

import java.util.Optional;

/**
 * This interface describes the API of the player service
 */
public interface PlayerManager {
    /**
     * Finds user by id
     * @param id of the player
     * @return an optional objects with the player if the player with such id exists
     */
    Optional<Player> findById(Long id);

    /**
     * Persists the player
     * @param player a player to store
     * @return a persisted player
     */
    Player save(Player player);
}
