package com.toptal.soccer.crypto;

import com.toptal.soccer.crypto.iface.PasswordHasher;
import org.apache.commons.lang3.Validate;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

public class SecretKeyPasswordHasher implements PasswordHasher {

    private static final int DEFAULT_ITERATIONS_COUNT = 10;
    private static final int DEFAULT_KEY_LENGTH = 512;

    private static final String DEFAULT_SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA512";
    private final int iterationsCount;
    private final int keyLength;
    private final SecretKeyFactory secretKeyFactory;

    private final byte[] salt;

    public SecretKeyPasswordHasher(final byte[] salt,
                                   final int iterationsCount,
                                   final int keyLength,
                                   final String secretKeyFactoryAlgorithm) throws Exception {

        Validate.notNull(salt, Constants.SALT_CANT_BE_NULL);
        Validate.isTrue(iterationsCount > 0, Constants.ITERATIONS_COUNT_SHOULD_BE_GREATER_THAN_ZERO);
        Validate.isTrue(keyLength > 0, Constants.KEY_LENGTH_SHOULD_BE_GREATER_THAN_ZERO);
        Validate.notEmpty(secretKeyFactoryAlgorithm, Constants.SECRET_KEY_FACTORY_ALGORITHM_CAN_T_BE_EMPTY);

        this.salt = salt;
        this.iterationsCount = iterationsCount;
        this.keyLength = keyLength;
        this.secretKeyFactory = SecretKeyFactory.getInstance(secretKeyFactoryAlgorithm);
    }

    public SecretKeyPasswordHasher(byte[] salt) throws Exception {

        this(salt, DEFAULT_ITERATIONS_COUNT, DEFAULT_KEY_LENGTH, DEFAULT_SECRET_KEY_FACTORY_ALGORITHM);
    }

    @Override
    public String hash(String password) throws Exception {

        Validate.notEmpty(password, Constants.PASSWORD_CAN_T_BE_EMPTY);
        Validate.notEmpty(password, Constants.SALT_CANT_BE_NULL);

        final PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, iterationsCount, keyLength);
        final byte[] hash = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();

        return Base64.getMimeEncoder().encodeToString(hash);
    }
}
