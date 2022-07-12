package com.toptal.soccer.orchestrator.config;

import com.toptal.soccer.manager.config.TransferManagerConfig;
import com.toptal.soccer.manager.config.UserManagerConfig;
import com.toptal.soccer.manager.iface.TransferManager;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.orchestrator.CompleteTransferOrchestratorImpl;
import com.toptal.soccer.orchestrator.iface.CompleteTransferOrchestrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration

@Import({TransferManagerConfig.class, UserManagerConfig.class})
public class CompleteTransferOrchestratorConfig {

    @Bean
    public CompleteTransferOrchestrator completeTransferOrchestrator(final TransferManager transferManager,
                                                                     final UserManager userManager,
                                                                     @Value("${soccer.orchestrator.transfer.complete.player.value.increment.percent.min:10}") final int playerValueIncrementPercentMin,
                                                                     @Value("${soccer.orchestrator.transfer.complete.player.value.increment.percent.max:100}") final int playerValueIncrementPercentMax) {
        return new CompleteTransferOrchestratorImpl(transferManager, userManager, playerValueIncrementPercentMin, playerValueIncrementPercentMax);
    }

}
