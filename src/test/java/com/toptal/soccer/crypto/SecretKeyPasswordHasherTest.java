package com.toptal.soccer.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SecretKeyPasswordHasherTest {

    @Test
    public void testHashesTheSamePasswordToTheSameHash() throws Exception{
        // given
        final byte[] salt = new RandomSaltGenerator().generate();

        final SecretKeyPasswordHasher secretKeyPasswordHasher = new SecretKeyPasswordHasher(salt);

        final String onePass = new String("one password");
        final String anotherOnePass = new String("one password");

        Assertions.assertNotSame(onePass, anotherOnePass);
        Assertions.assertEquals(onePass, anotherOnePass);



        // when
        final String firstHash = secretKeyPasswordHasher.hash(onePass);
        final String anotherHash = secretKeyPasswordHasher.hash(anotherOnePass);

        // then
        Assertions.assertEquals(firstHash, anotherHash);

    }

    @Test
    public void testHashesToADifferentHashIfThePasswordsAreDifferent() throws Exception{
        // given
        final byte[] salt = new RandomSaltGenerator().generate();

        final SecretKeyPasswordHasher secretKeyPasswordHasher = new SecretKeyPasswordHasher(salt);

        final String onePass = "one password";
        final String anotherPass = "one Password";

        Assertions.assertNotEquals(onePass, anotherPass);

        // when
        final String firstHash = secretKeyPasswordHasher.hash(onePass);
        final String anotherHash = secretKeyPasswordHasher.hash(anotherPass);

        // then
        Assertions.assertNotEquals(firstHash, anotherHash);

    }
}
