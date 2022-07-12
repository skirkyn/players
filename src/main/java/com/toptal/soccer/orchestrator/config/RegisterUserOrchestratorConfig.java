package com.toptal.soccer.orchestrator.config;

import com.toptal.soccer.crypto.SecretKeyPasswordHasher;
import com.toptal.soccer.crypto.iface.PasswordHasher;
import com.toptal.soccer.manager.config.UserManagerConfig;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.orchestrator.RegisterUserOrchestratorImpl;
import com.toptal.soccer.orchestrator.iface.CreateNewTeamOrchestrator;
import com.toptal.soccer.orchestrator.iface.RegisterUserOrchestrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({UserManagerConfig.class, CreateNewTeamOrchestratorConfig.class})
public class RegisterUserOrchestratorConfig {


    @Bean
    public PasswordHasher passwordHasher(@Value("${soccer.orchestrator.user.password.hasher.salt}") final byte[] salt,
                                         @Value("${soccer.orchestrator.user.password.hasher.iterations.count}") final int iterationsCount,
                                         @Value("${soccer.orchestrator.user.password.hasher.key.length}") final int keyLength,
                                         @Value("${soccer.orchestrator.user.password.hasher.secret.factory.algorithm}") final String secretFactoryAlgorithm) throws Exception {
        return new SecretKeyPasswordHasher(salt, iterationsCount, keyLength, secretFactoryAlgorithm);
    }

    @Bean
    public RegisterUserOrchestrator registerUserOrchestrator(final UserManager userManager,
                                                             final PasswordHasher passwordHasher,
                                                             final CreateNewTeamOrchestrator createNewTeamOrchestrator) {

        return new RegisterUserOrchestratorImpl(userManager, passwordHasher, createNewTeamOrchestrator);
    }

}
