package com.toptal.soccer.manager;

import com.toptal.soccer.manager.iface.PlayerManager;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.repo.PlayerRepo;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.Validate;

import java.util.Optional;

/**
 * This class implements player manager functionality
 */
public class PlayerManagerImpl implements PlayerManager {

    private final PlayerRepo playerRepo;

    public PlayerManagerImpl(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @Override
    @Transactional
    public Optional<Player> findById(Long id) {

        Validate.notNull(id, Constants.ID_CAN_T_BE_NULL);

        return playerRepo.findById(id);
    }

    @Override
    @Transactional
    public Player save(Player player) {
        Validate.notNull(player, Constants.
                PLAYER_ID_CAN_T_BE_NULL);

        return playerRepo.save(player);
    }
}
