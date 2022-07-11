package com.toptal.soccer.manager.iface;

import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * This interface describes the API of the team service
 */
public interface TeamManager {
    /**
     * Searches team by id
     * @param id id of the team

     * @return an optional that has a team inside if the team exists
     */
    Optional<Team> findById(Long id);


    /**
     * Find team players by team id
     * @param teamId id of the team
     * @param start start
     * @param pageSize num of the results to return
     * @return list of players capped with page size, and started with start
     */
    List<Player> findPlayersByTeamId(Long teamId, int start, int pageSize);
    /**
     * Persists the team
     * @param team a team to store
     * @return a persisted team
     */
    Team save(Team team);

    /**
     * Returns total team value for the team id
     * @param id id on the team
     * @return team value
     */
    BigDecimal findTotalTeamValue(Long id);
}
