package com.toptal.soccer.orchestrator.iface;

import com.toptal.soccer.model.Player;

/**
 * This interface describes a specification for the classes that will want to provide the player creation functionality
 */
public interface CreateNewPlayerOrchestrator {

    /**
     * Creates a new player
     * @return a new player
     */
    Player create(Player.Type type);
}
