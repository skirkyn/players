package com.toptal.soccer.orchestrator.config;

import com.toptal.soccer.crypto.config.CryptoConfig;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.crypto.iface.PasswordHasher;
import com.toptal.soccer.manager.config.UserManagerConfig;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.orchestrator.LoginUserOrchestratorImpl;
import com.toptal.soccer.orchestrator.iface.LoginUserOrchestrator;
import com.toptal.soccer.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({UserManagerConfig.class, CryptoConfig.class})
public class LoginUserOrchestratorConfig {


    @Bean
    public JWTGenerator jwtGenerator(@Value("${soccer.security.jwt.token.sec.valid:3600}") final long secondsValid,
                                     @Value("${soccer.security.jwt.token.secret}") final String secret,
                                     final Crypto crypto) {
        return new JWTGenerator(secondsValid, secret, crypto);
    }

    @Bean
    public LoginUserOrchestrator loginUserOrchestrator(final UserManager userManager, final PasswordHasher passwordHasher, final JWTGenerator jwtGenerator) {
        return new LoginUserOrchestratorImpl(userManager, passwordHasher, jwtGenerator);
    }

}
