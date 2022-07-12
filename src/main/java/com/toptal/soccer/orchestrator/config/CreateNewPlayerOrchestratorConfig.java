package com.toptal.soccer.orchestrator.config;

import com.github.javafaker.Name;
import com.toptal.soccer.generator.FakerFullNameGenerator;
import com.toptal.soccer.generator.RandomDOBGenerator;
import com.toptal.soccer.generator.config.GeneratorConfig;
import com.toptal.soccer.orchestrator.CreateNewPlayerOrchestratorImpl;
import com.toptal.soccer.orchestrator.iface.CreateNewPlayerOrchestrator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@Import(GeneratorConfig.class)
@Configuration
public class CreateNewPlayerOrchestratorConfig {
    @Bean
    public Supplier<Name> fullNameGenerator() {
        return new FakerFullNameGenerator();
    }

    @Bean
    public Supplier<LocalDateTime> dobGenerator(@Value("${soccer.orchestrator.player.new.age.min:18}") final int playerMinAge,
                                                @Value("${soccer.orchestrator.player.new.age.max:40}") final int playerMaxAge) {
        return new RandomDOBGenerator(playerMinAge, playerMaxAge);
    }

    @Bean
    public CreateNewPlayerOrchestrator createNewPlayerOrchestrator(final Supplier<Name> fullNameGenerator,
                                                                   final Supplier<LocalDateTime> dobGenerator,
                                                                   final Supplier<String> countryGenerator,
                                                                   @Value("${soccer.orchestrator.player.new.default.value:1000000}") final BigDecimal defaultValue) {
        return new CreateNewPlayerOrchestratorImpl(fullNameGenerator, dobGenerator, countryGenerator, defaultValue);
    }

}
