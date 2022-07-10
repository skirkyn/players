package com.toptal.soccer.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class FakerCountryGeneratorTest {

    @Test
    public void testGeneratesCountry() {
        Assertions.assertNotNull(new FakerCountryGenerator().get());
    }
}
