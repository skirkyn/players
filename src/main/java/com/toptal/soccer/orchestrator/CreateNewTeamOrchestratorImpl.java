package com.toptal.soccer.orchestrator;

import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import com.toptal.soccer.orchestrator.iface.CreateNewPlayerOrchestrator;
import com.toptal.soccer.orchestrator.iface.CreateNewTeamOrchestrator;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class can create a new team with default settings
 */
public class CreateNewTeamOrchestratorImpl implements CreateNewTeamOrchestrator {

    private final CreateNewPlayerOrchestrator createNewPlayerOrchestrator;
    private final Map<Player.Type, Integer> playersToCreate;
    private final BigDecimal defaultBudget;
    private final Supplier<String> teamNameGenerator;
    final Supplier<String> countryGenerator;

    public CreateNewTeamOrchestratorImpl(final CreateNewPlayerOrchestrator createNewPlayerOrchestrator,
                                         final Supplier<String> teamNameGenerator,
                                         final Supplier<String> countryGenerator,
                                         final Map<Player.Type, Integer> playersToCreate,
                                         final BigDecimal defaultBudget) {

        Validate.notNull(playersToCreate, Constants.PLAYERS_SPECIFICATION_CAN_T_BE_NULL);

        this.createNewPlayerOrchestrator = createNewPlayerOrchestrator;
        this.playersToCreate = playersToCreate;
        this.defaultBudget = defaultBudget;
        this.teamNameGenerator = teamNameGenerator;
        this.countryGenerator = countryGenerator;
    }

    @Override
    public Team create() {
        final Team team = new Team();
        team.setBudget(defaultBudget);
        team.setName(teamNameGenerator.get());
        team.setCountry(countryGenerator.get());
        team.setPlayers(playersToCreate.entrySet()
                .stream()
                .flatMap(e -> Stream.generate(() -> createNewPlayerOrchestrator.create(e.getKey()))
                                    .limit(e.getValue()))
                .collect(Collectors.toList()));
        return team;
    }
}
