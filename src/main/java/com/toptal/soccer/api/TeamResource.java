package com.toptal.soccer.api;

import com.toptal.soccer.api.exception.NotAuthorizedException;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.Player;
import com.toptal.soccer.dto.Team;
import com.toptal.soccer.manager.iface.TeamManager;
import com.toptal.soccer.manager.iface.UserManager;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This class defines all web API that logically belongs to the Team resource
 */
@RestController
public class TeamResource {

    private static final String LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_TEAM =
            "Logged user is not authorized to access this team: ";
    private final UserManager userManager;
    private final TeamManager teamManager;
    private final Function<Team, com.toptal.soccer.model.Team> teamDTOToTeam;
    private final BiFunction<com.toptal.soccer.model.Team, BigDecimal, Team> teamToTeamDTO;
    private final Function<com.toptal.soccer.model.Player, Player> playerToPlayerDTO;
    private final Crypto crypto;

    public TeamResource(final UserManager userManager,
                        final TeamManager teamManager,
                        final Function<Team, com.toptal.soccer.model.Team> teamDTOToTeam,
                        final BiFunction<com.toptal.soccer.model.Team, BigDecimal, Team> teamToTeamDTO,
                        final Function<com.toptal.soccer.model.Player, Player> playerToPlayerDTO,
                        final Crypto crypto) {
        this.userManager = userManager;
        this.teamManager = teamManager;
        this.teamDTOToTeam = teamDTOToTeam;
        this.teamToTeamDTO = teamToTeamDTO;
        this.playerToPlayerDTO = playerToPlayerDTO;
        this.crypto = crypto;
    }

    /**
     * This method copies name and country from the team object that is passed to the method
     *
     * @param id             - team id, should not be null. The authenticated user has to own the team whose id is passed in request.
     * @param authentication current authentication
     * @return a response with a team entity as a body
     */
    @PatchMapping(path = "/team/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Team update(@RequestBody final Team team, @PathVariable final String id, final Authentication authentication) {

        final Long decrypted = Long.valueOf(crypto.decrypt(id));

        if (!Objects.equals(authentication.getPrincipal(), userManager.findUserTeam(decrypted).getId())) {
            throw new NotAuthorizedException(LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_TEAM + id);
        }

        return teamToTeamDTO.apply(teamDTOToTeam.apply(team), teamManager.findTotalTeamValue(decrypted));
    }

    /**
     * This method returns team players by team id.
     *
     * @param id             team id, should not be null. The authenticated user has to own the team whose id is passed in request.
     * @param start          - parameter for the pagination. Defines the beginning of the result list.Optional. Default = 0
     * @param size           - parameter for the pagination. Defines the size of the list to return. Optional. Default = 0
     * @param authentication current authentication
     *
     * @return a list of players that belong to the team, capped with the pagination parameters
     */
    @GetMapping(path = "/team/{id}/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Player> findPlayers(@PathVariable String id,
                                    @RequestParam(defaultValue = "0", required = false) int start,
                                    @RequestParam(defaultValue = "20", required = false) int size,
                                    final Authentication authentication) {
        final Long decrypted = Long.valueOf(crypto.decrypt(id));

        if (!Objects.equals(authentication.getPrincipal(), userManager.findUserTeam(decrypted).getId())) {
            throw new NotAuthorizedException(LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_TEAM + id);
        }

        return teamManager.findPlayersByTeamId(decrypted, start, size).stream().map(playerToPlayerDTO).toList();
    }
}
