package com.toptal.soccer.crypto;

import com.toptal.soccer.crypto.iface.Crypto;
import org.apache.commons.lang3.Validate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AESCrypto implements Crypto {

    public static final String DEFAULT_TRANSFORMATION = "AES/ECB/PKCS5PADDING";
    public static final String DEFAULT_KEY_HASHING_ALGORITHM = "SHA-1";
    private final String transformation;
    private final SecretKey secretKey;

    public AESCrypto(final String secret, final String transformation, final String keyHashingAlgorithm) throws NoSuchAlgorithmException {


        Validate.notEmpty(secret, Constants.SECRET_CAN_T_BE_EMPTY);
        Validate.notEmpty(transformation, Constants.TRANSFORMATION_CAN_T_BE_EMPTY);
        Validate.notEmpty(keyHashingAlgorithm, Constants.KEY_HASHING_ALGORITHM_CAN_T_BE_EMPTY);

        this.transformation = transformation;
        this.secretKey = getKey(secret, keyHashingAlgorithm);
    }

    public AESCrypto(final String secretKey) throws NoSuchAlgorithmException {
        this(secretKey, DEFAULT_TRANSFORMATION, DEFAULT_KEY_HASHING_ALGORITHM);
    }


    private SecretKey getKey(final String secret, final String hashingAlgorithm) throws NoSuchAlgorithmException {

        Validate.notEmpty(secret, Constants.SECRET_CAN_T_BE_EMPTY);
        Validate.notEmpty(hashingAlgorithm, Constants.KEY_HASHING_ALGORITHM_CAN_T_BE_EMPTY);


        final MessageDigest sha = MessageDigest.getInstance(hashingAlgorithm);
        final byte[] key = Arrays.copyOf(sha.digest(secret.getBytes(StandardCharsets.UTF_8)), 16);
        return new SecretKeySpec(key, "AES");

    }

    @Override
    public String encrypt(String plainText) {

        Validate.notEmpty(plainText, Constants.PLAIN_TEXT_CAN_T_BE_EMPTY);

        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String decrypt(String cipherText) {

        Validate.notEmpty(cipherText, Constants.CIPHER_TEXT_CAN_T_BE_EMPTY);

        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
