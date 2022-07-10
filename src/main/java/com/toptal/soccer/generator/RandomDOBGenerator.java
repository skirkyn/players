package com.toptal.soccer.generator;

import org.apache.commons.lang3.Validate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import static java.time.ZoneOffset.UTC;

/**
 * Generates a DOB in a range
 */
public class RandomDOBGenerator implements Supplier<LocalDateTime> {

    private final long startAge;
    private final long endAge;

    public RandomDOBGenerator(final long startAge, final long endAge) {
        Validate.isTrue(startAge > 0 && startAge < endAge, Constants.AGE_SHOULD_BE_IN_A_RANGE);
        this.startAge = startAge;
        this.endAge = endAge;
    }

    @Override
    public LocalDateTime get() {
        final LocalDateTime now = LocalDateTime.now(UTC);

        final LocalDateTime periodStart = now.minusYears(endAge);
        final LocalDateTime periodEnd = now.minusYears(startAge);

        return LocalDateTime.ofInstant(Instant.ofEpochSecond(ThreadLocalRandom.current()
                .nextLong(periodStart.atZone(UTC).toInstant().getEpochSecond(),
                        periodEnd.atZone(UTC).toInstant().getEpochSecond())), UTC);
    }
}
