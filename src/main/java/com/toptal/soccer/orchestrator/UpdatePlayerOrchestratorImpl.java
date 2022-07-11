package com.toptal.soccer.orchestrator;

import com.toptal.soccer.manager.iface.PlayerManager;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.orchestrator.iface.UpdatePlayerOrchestrator;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.Validate;

/**
 * This class can update a player's names and country fields
 */
public class UpdatePlayerOrchestratorImpl implements UpdatePlayerOrchestrator {
    private final PlayerManager playerManager;

    public UpdatePlayerOrchestratorImpl(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }


    @Override
    @Transactional
    public Player update(Player player) {

        Validate.notNull(player, Constants.PLAYER_CAN_T_BE_NULL);
        Validate.notNull(player.getId(), Constants.PLAYER_ID_CAN_T_BE_NULL);


        Validate.notEmpty(player.getCountry(), Constants.COUNTRY_CANT_BE_EMPTY);
        Validate.notEmpty(player.getFirstName(), Constants.FIRST_NAME_CANT_BE_EMPTY);
        Validate.notEmpty(player.getLastName(), Constants.LAST_NAME_CANT_BE_EMPTY);

        final Player fromDb = playerManager.findById(player.getId()).orElseThrow(() ->
                new IllegalArgumentException(Constants.PLAYER_CAN_T_BE_NULL));
        fromDb.setCountry(player.getCountry());
        fromDb.setFirstName(player.getFirstName());
        fromDb.setLastName(player.getLastName());

        return playerManager.save(fromDb);
    }
}
