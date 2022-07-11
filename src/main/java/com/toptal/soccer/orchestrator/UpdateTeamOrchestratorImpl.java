package com.toptal.soccer.orchestrator;

import com.toptal.soccer.manager.iface.TeamManager;
import com.toptal.soccer.model.Team;
import com.toptal.soccer.orchestrator.iface.UpdateTeamOrchestrator;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.Validate;

/**
 * This class can update a team's name and country fields
 */
public class UpdateTeamOrchestratorImpl implements UpdateTeamOrchestrator {
    private final TeamManager teamManager;

    public UpdateTeamOrchestratorImpl(TeamManager teamManager) {
        this.teamManager = teamManager;
    }


    @Override
    @Transactional
    public Team update(Team team) {

        Validate.notNull(team, Constants.PLAYER_CAN_T_BE_NULL);
        Validate.notNull(team.getId(), Constants.PLAYER_ID_CAN_T_BE_NULL);


        Validate.notEmpty(team.getCountry(), Constants.COUNTRY_CANT_BE_EMPTY);

        Validate.notEmpty(team.getName(), Constants.NAME_CANT_BE_EMPTY);

        final Team fromDb = teamManager.findById(team.getId()).orElseThrow(() ->
                new IllegalArgumentException(Constants.PLAYER_CAN_T_BE_NULL));
        fromDb.setCountry(team.getCountry());
        fromDb.setName(team.getName());

        return teamManager.save(fromDb);
    }
}
