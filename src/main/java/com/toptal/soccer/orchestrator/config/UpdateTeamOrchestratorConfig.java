package com.toptal.soccer.orchestrator.config;

import com.toptal.soccer.manager.config.TeamManagerConfig;
import com.toptal.soccer.manager.iface.TeamManager;
import com.toptal.soccer.orchestrator.UpdateTeamOrchestratorImpl;
import com.toptal.soccer.orchestrator.iface.UpdateTeamOrchestrator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TeamManagerConfig.class)
public class UpdateTeamOrchestratorConfig {


    @Bean
    public UpdateTeamOrchestrator updateTeamOrchestrator(final TeamManager teamManager) {
        return new UpdateTeamOrchestratorImpl(teamManager);
    }

}
