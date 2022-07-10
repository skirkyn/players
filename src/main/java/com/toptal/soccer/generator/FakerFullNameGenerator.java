package com.toptal.soccer.generator;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import java.util.function.Supplier;

/**
 * Generates team name with help oh faker library
 */
public class FakerFullNameGenerator implements Supplier<Name> {

    private final Faker faker;

    public FakerFullNameGenerator(){
        this.faker = new Faker();
    }

    @Override
    public Name get() {
        return faker.name();
    }
}
