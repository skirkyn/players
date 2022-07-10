package com.toptal.soccer.generator;

import com.github.javafaker.Faker;

import java.util.function.Supplier;

/**
 * This class generates random countries
 */
public class FakerCountryGenerator implements Supplier<String> {

    private final Faker faker;

    public FakerCountryGenerator() {
        this.faker = new Faker();
    }


    @Override
    public String get() {
        return faker.address().country();
    }
}
