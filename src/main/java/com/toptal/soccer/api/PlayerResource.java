package com.toptal.soccer.api;

import com.toptal.soccer.api.exception.NotAuthorizedException;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.Player;
import com.toptal.soccer.manager.iface.PlayerManager;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.orchestrator.iface.UpdatePlayerOrchestrator;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.function.Function;

/**
 * This class defines all web API that logically belongs to the Player resource
 */
@RestController
public class PlayerResource extends BaseResource{
    private static final String LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_PLAYER =
            "Logged user is not authorized to access this player: ";
    private final Crypto crypto;
    private final UserManager userManager;
    private final PlayerManager playerManager;
    private final Function<com.toptal.soccer.model.Player, Player> playerToPlayerDTO;
    private final Function<Player, com.toptal.soccer.model.Player> playerDTOToPlayer;

    private final UpdatePlayerOrchestrator updatePlayerOrchestrator;

    public PlayerResource(final Crypto crypto,
                          final UserManager userManager,
                          final PlayerManager playerManager,
                          final Function<com.toptal.soccer.model.Player, Player> playerToPlayerDTO,
                          final Function<Player, com.toptal.soccer.model.Player> playerDTOToPlayer, UpdatePlayerOrchestrator updatePlayerOrchestrator) {
        this.crypto = crypto;
        this.userManager = userManager;
        this.playerManager = playerManager;
        this.playerToPlayerDTO = playerToPlayerDTO;
        this.playerDTOToPlayer = playerDTOToPlayer;
        this.updatePlayerOrchestrator = updatePlayerOrchestrator;
    }

    /**
     * This method searches for a player by their id
     *
     * @param id             id of the player
     * @param authentication current authentication
     * @return a player
     */
    @GetMapping(path = "/player/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Player find(@PathVariable("id") String id, final Authentication authentication) {

        final Long decrypted = Long.valueOf(crypto.decrypt(id));

        userManager.findByPlayerId(decrypted).filter(u -> Objects.equals(authentication.getPrincipal(), u.getId()))
                .orElseThrow(() -> new NotAuthorizedException(LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_PLAYER + id));

        return playerManager.findById(decrypted).map(playerToPlayerDTO).orElseThrow(() -> new IllegalArgumentException("Player doesn't exist with id: " + id));

    }

    /**
     * This method updates a player. Only first, last names and a country can be updated.
     *
     * @param player         a player object with new values of the fields
     * @param id             id of the player
     * @param authentication current authentication
     * @return an updated player
     */
    @PatchMapping(path = "/player/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Player update(@RequestBody Player player, @PathVariable("id") String id, final Authentication authentication) {
        final Long decrypted = Long.valueOf(crypto.decrypt(id));

        userManager.findByPlayerId(decrypted).filter(u -> Objects.equals(authentication.getPrincipal(), u.getId()))
                .orElseThrow(() -> new NotAuthorizedException(LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_PLAYER + id));

        return playerToPlayerDTO.apply(updatePlayerOrchestrator.update(playerDTOToPlayer.apply(player)));
    }
}
