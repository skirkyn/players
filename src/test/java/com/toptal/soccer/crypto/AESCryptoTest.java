package com.toptal.soccer.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AESCryptoTest {


    @Test
    public void testCanEncryptAndDecryptsToOriginalPlainText() throws Exception {
        final AESCrypto aesCrypto = new AESCrypto("some key");

        // given
        final String plainText = "some plain text";

        // when
        final String cipherText = aesCrypto.encrypt(plainText);
        Assertions.assertNotEquals(plainText, cipherText);

        final String plainTextAfterDecryption = aesCrypto.decrypt(cipherText);

        // then
        Assertions.assertEquals(plainText, plainTextAfterDecryption);

    }

}
