package com.toptal.soccer.generator.config;

import com.toptal.soccer.generator.FakerCountryGenerator;
import com.toptal.soccer.generator.FakerTeamNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class GeneratorConfig {

    @Bean
    public Supplier<String> countryGenerator() {
        return new FakerCountryGenerator();
    }

    @Bean
    public Supplier<String> teamNameGenerator() {
        return new FakerTeamNameGenerator();
    }

}
