package com.toptal.soccer.crypto.iface;

/**
 * This interface defines a specification for encryption and decryption functionality
 */
public interface Crypto {

    /**
     * Encrypts given plain text
     * @param plainText a plain text
     * @return cipher text. Encryption algorithm should be defined by the implementors
     */
   String encrypt(final String plainText) throws Exception;

    /**
     * Decrypts given cipher text to a plain text
     * @param cipherText a cipher text
     * @return plain text. Decryption algorithm should be defined by the implementors
     */
   String decrypt(final String cipherText) throws Exception;

}
