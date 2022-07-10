package com.toptal.soccer.generator;

/**
 * Provides some utility methods
 */
public final class Util {
    private Util() {

    }

    /**
     * Randomly chooses an integer from the range
     * @param min start of the range inclusive
     * @param max end of the range exclusive
     *
     * @return a random integer from the range
     */
    public static int randomFromRange(final int min, final int max) {
        return (int) ((Math.random() * (max - min)) + min);

    }
}
