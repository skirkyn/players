package com.toptal.soccer.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Supplier;

public class RandomDOBGeneratorTest {

    @Test
    public void testGeneratesDateFromARange() {
        final int startAge = 18;
        final int endAge = 40;
        final Supplier<LocalDateTime> generator = new RandomDOBGenerator(startAge, endAge);

        final int thisYear = LocalDate.now().getYear();
        for (int i = 0; i < 10_000; i++) {
            final LocalDateTime generatedDate = generator.get();

            final int years = thisYear - generatedDate.getYear();
            Assertions.assertTrue(years >= 18 && years <= endAge);
        }

    }
}
