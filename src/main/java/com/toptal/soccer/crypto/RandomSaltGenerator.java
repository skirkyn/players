package com.toptal.soccer.crypto;

import com.toptal.soccer.crypto.iface.SaltGenerator;
import org.apache.commons.lang3.Validate;

import java.security.SecureRandom;

/**
 * A salt generator that uses secure random to generate salt
 */
public class RandomSaltGenerator implements SaltGenerator {

    private final static int DEFAULT_SALT_LENGTH = 12;
    private final int defaultLength;
    private final SecureRandom secureRandom;

    public RandomSaltGenerator(final int defaultLength) {
        Validate.isTrue(defaultLength > 0, Constants.SALT_LENGTH_SHOULD_BE_GREATER_THAN_ZERO);
        this.defaultLength = defaultLength;
        this.secureRandom = new SecureRandom();
    }

    public RandomSaltGenerator() {
        this(DEFAULT_SALT_LENGTH);
    }

    @Override
    public byte[] generate() {
        return secureRandom.generateSeed(defaultLength);
    }

    @Override
    public byte[] generate(int numOfBytes) {
        Validate.isTrue(defaultLength > 0, Constants.SALT_LENGTH_SHOULD_BE_GREATER_THAN_ZERO);

        return secureRandom.generateSeed(numOfBytes);
    }
}
