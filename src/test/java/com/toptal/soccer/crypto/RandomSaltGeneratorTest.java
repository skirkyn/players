package com.toptal.soccer.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandomSaltGeneratorTest {

    @Test
    public void testGeneratesDefaultLengthSalt(){
        // given
        final int defaultLength = 12;
        final RandomSaltGenerator randomSaltGenerator = new RandomSaltGenerator(defaultLength);

        // when
        final byte[] salt = randomSaltGenerator.generate();

        // then
        Assertions.assertEquals(defaultLength, salt.length);
    }

    @Test
    public void testGeneratesDCustomLengthSalt(){
        // given
        final int defaultLength = 12;
        final int customLength = 13;
        final RandomSaltGenerator randomSaltGenerator = new RandomSaltGenerator(defaultLength);

        // when
        final byte[] salt = randomSaltGenerator.generate(customLength);

        // then
        Assertions.assertEquals(customLength, salt.length);
    }
}
