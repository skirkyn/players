package com.toptal.soccer.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class FakerTeamNameGeneratorTest{

    @Test
    public void testGeneratesTeamName() {
        Assertions.assertNotNull(new FakerTeamNameGenerator().get());
    }
}
