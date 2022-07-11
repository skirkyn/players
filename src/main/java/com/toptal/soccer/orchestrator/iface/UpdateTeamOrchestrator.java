package com.toptal.soccer.orchestrator.iface;

import com.toptal.soccer.model.Team;

/**
 * A specification for an update team action
 */
public interface UpdateTeamOrchestrator {

    /**
     * Updates existing team name and country
     *
     * @param team that carries new data
     * @return a merged team
     */
    Team update(Team team);
}
