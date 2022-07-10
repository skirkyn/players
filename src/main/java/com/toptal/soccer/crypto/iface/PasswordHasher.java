package com.toptal.soccer.crypto.iface;

/**
 * This interface describes the API of the class that wishes to me a password hasher.
 */
public interface PasswordHasher {

    /**
     * Hashes the password with the salt provided
     * @param password a password to hash
     *
     * @return base64 encoded hashed password
     */
    String hash(final String password) throws Exception;
    
}
