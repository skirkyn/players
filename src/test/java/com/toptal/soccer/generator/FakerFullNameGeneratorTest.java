package com.toptal.soccer.generator;

import com.github.javafaker.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FakerFullNameGeneratorTest {
    @Test
    public void testGeneratesFullName() {
        final Name  name = new FakerFullNameGenerator().get();
        Assertions.assertNotNull(name);
        Assertions.assertNotNull(name.firstName());
        Assertions.assertNotNull(name.lastName());
    }
}
