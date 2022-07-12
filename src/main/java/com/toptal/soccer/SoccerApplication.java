package com.toptal.soccer;

import com.toptal.soccer.manager.config.TransferManagerConfig;
import com.toptal.soccer.orchestrator.config.CompleteTransferOrchestratorConfig;
import com.toptal.soccer.orchestrator.config.RegisterUserOrchestratorConfig;
import com.toptal.soccer.orchestrator.config.UpdatePlayerOrchestratorConfig;
import com.toptal.soccer.orchestrator.config.UpdateTeamOrchestratorConfig;
import com.toptal.soccer.security.config.SecurityConfig;
import com.toptal.soccer.transformer.config.TransformerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import({RegisterUserOrchestratorConfig.class,
        CompleteTransferOrchestratorConfig.class,
        UpdatePlayerOrchestratorConfig.class,
        UpdateTeamOrchestratorConfig.class,
        TransferManagerConfig.class,
        TransformerConfig.class,
        SecurityConfig.class
})
@EnableJpaRepositories(basePackages = {"com.toptal.soccer.repo"})
@SpringBootApplication()
public class SoccerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoccerApplication.class, args);
    }

}
