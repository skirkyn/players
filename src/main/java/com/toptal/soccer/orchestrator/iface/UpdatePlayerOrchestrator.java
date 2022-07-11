package com.toptal.soccer.orchestrator.iface;

import com.toptal.soccer.model.Player;

/**
 * A specification for an update player action
 */
public interface UpdatePlayerOrchestrator {

    /**
     * Updates existing player names and country
     *
     * @param player that carries new data
     * @return a merged player
     */
    Player update(Player player);
}
