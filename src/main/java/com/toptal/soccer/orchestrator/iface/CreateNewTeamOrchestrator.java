package com.toptal.soccer.orchestrator.iface;

import com.toptal.soccer.model.Team;

/**
 * This interface describes a specification for the classes that will want to provide the team creation functionality
 */
public interface CreateNewTeamOrchestrator {

    /**
     * Creates a new team
     * @return a new team
     */
    Team create();
}
