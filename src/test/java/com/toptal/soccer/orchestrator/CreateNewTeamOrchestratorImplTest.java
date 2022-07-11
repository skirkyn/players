package com.toptal.soccer.orchestrator;

import com.github.javafaker.Name;
import com.toptal.soccer.model.Currency;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import com.toptal.soccer.orchestrator.iface.CreateNewPlayerOrchestrator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateNewTeamOrchestratorImplTest{

    @Test
    public void testGeneratesTeamWithPlayers() {
        // given
        final Name name = mock(Name.class);
        final String first = "first";
        final String last = "last";
        final LocalDateTime now = LocalDateTime.now();
        final String country = "country";
        final BigDecimal defaultValue = BigDecimal.valueOf(1_00_000);
        final String teamName = "team";
        final BigDecimal defaultBudget = BigDecimal.valueOf(5_000_000L);
        final CreateNewPlayerOrchestrator playerOrchestrator = new CreateNewPlayerOrchestratorImpl(
                () -> name,
                () -> now,
                () -> country,
                defaultValue

        );

        final Map<Player.Type, Integer> playersToCreate = Map.of(Player.Type.GOALKEEPER, 3,
                Player.Type.DEFENDER, 6,
                Player.Type.MIDFIELDER, 6,
                Player.Type.ATTACKER, 6);

        // when
        when(name.firstName()).thenReturn(first);
        when(name.lastName()).thenReturn(last);


        final CreateNewTeamOrchestratorImpl createNewTeamOrchestrator = new CreateNewTeamOrchestratorImpl(
                playerOrchestrator,
                () -> teamName,
                () -> country,
                playersToCreate,
                defaultBudget

        );

        final Team team = createNewTeamOrchestrator.create();

        //then
        Assertions.assertEquals(defaultBudget, team.getBudget());
        Assertions.assertEquals(Currency.DOLLAR, team.getBudgetCurrency());
        Assertions.assertEquals(country, team.getCountry());
        Assertions.assertEquals(playersToCreate,
                team.getPlayers().stream().collect(Collectors.groupingBy(Player::getType, Collectors.summingInt(e -> 1))));


    }

    @Test
    public void testGeneratesTeamWithNoPlayersIfThatIsSpecified() {
        // given
        final Name name = mock(Name.class);
        final String first = "first";
        final String last = "last";
        final LocalDateTime now = LocalDateTime.now();
        final String country = "country";
        final BigDecimal defaultValue = BigDecimal.valueOf(1_000_000);
        final String teamName = "team";
        final BigDecimal defaultBudget = BigDecimal.valueOf(5_000_000L);
        final CreateNewPlayerOrchestrator playerOrchestrator = new CreateNewPlayerOrchestratorImpl(
                () -> name,
                () -> now,
                () -> country,
                defaultValue

        );

        final Map<Player.Type, Integer> playersToCreate = Collections.emptyMap();

        // when
        when(name.firstName()).thenReturn(first);
        when(name.lastName()).thenReturn(last);


        final CreateNewTeamOrchestratorImpl createNewTeamOrchestrator = new CreateNewTeamOrchestratorImpl(
                playerOrchestrator,
                () -> teamName,
                () -> country,
                playersToCreate,
                defaultBudget

        );

        final Team team = createNewTeamOrchestrator.create();

        //then
        Assertions.assertEquals(defaultBudget, team.getBudget());
        Assertions.assertEquals(Currency.DOLLAR, team.getBudgetCurrency());
        Assertions.assertEquals(country, team.getCountry());
        Assertions.assertEquals(0, team.getPlayers().size());


    }
}
