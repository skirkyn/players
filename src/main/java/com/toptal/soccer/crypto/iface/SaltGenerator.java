package com.toptal.soccer.crypto.iface;

/**
 * Interface provides a specification for a class that wishes to generate salt for hashing
 */
public interface SaltGenerator {

    /**
     * Generate salt with the default number of bytes. Default number of bytes shpould be defined by an implementer
     *
     * @return the salt
     */
    byte[] generate();

    /**
     * Generate salt with the number of bytes == numOfBytes.
     * @param numOfBytes length of the salt
     * @return the salt
     */
    byte[] generate(int numOfBytes);
}
