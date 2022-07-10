package com.toptal.soccer.generator;

import com.github.javafaker.Faker;

import java.util.function.Supplier;

/**
 * Generates team name with help oh faker library
 */
public class FakerTeamNameGenerator implements Supplier<String> {

    private final Faker faker;

    public FakerTeamNameGenerator(){
        this.faker = new Faker();
    }

    @Override
    public String get() {
        return faker.team().name();
    }
}
