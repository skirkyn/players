package com.toptal.soccer.orchestrator.config;

import com.toptal.soccer.manager.config.PlayerManagerConfig;
import com.toptal.soccer.manager.iface.PlayerManager;
import com.toptal.soccer.orchestrator.UpdatePlayerOrchestratorImpl;
import com.toptal.soccer.orchestrator.iface.UpdatePlayerOrchestrator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PlayerManagerConfig.class)
public class UpdatePlayerOrchestratorConfig {


    @Bean
    public UpdatePlayerOrchestrator updatePlayerOrchestrator(final PlayerManager playerManager) {
        return new UpdatePlayerOrchestratorImpl(playerManager);
    }

}
