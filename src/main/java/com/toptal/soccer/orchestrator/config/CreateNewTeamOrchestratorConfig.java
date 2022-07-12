package com.toptal.soccer.orchestrator.config;

import com.toptal.soccer.generator.config.GeneratorConfig;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.orchestrator.CreateNewTeamOrchestratorImpl;
import com.toptal.soccer.orchestrator.iface.CreateNewPlayerOrchestrator;
import com.toptal.soccer.orchestrator.iface.CreateNewTeamOrchestrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Supplier;

@Configuration
@Import({CreateNewPlayerOrchestratorConfig.class, GeneratorConfig.class})
public class CreateNewTeamOrchestratorConfig {

    @Bean
    public CreateNewTeamOrchestrator createNewTeamrOrchestrator(
            final CreateNewPlayerOrchestrator createNewPlayerOrchestrator,
            final Supplier<String> teamNameGenerator,
            final Supplier<String> countryGenerator,
            @Value("#{${soccer.orchestrator.team.new.default.players}}") final Map<Player.Type, Integer> playersToCreate,
            @Value("${soccer.orchestrator.team.new.default.budget:5000000}") final BigDecimal defaultBudget) {
        return new CreateNewTeamOrchestratorImpl(createNewPlayerOrchestrator,
                teamNameGenerator,
                countryGenerator,
                playersToCreate,
                defaultBudget);
    }

}
