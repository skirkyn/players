package com.toptal.soccer.security;

import com.toptal.soccer.TestUtil;
import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class JwtGeneratorAndValidatorTest {

    private static final long SEC_VALID = 2;
    private static final String JWT_SECRET = "jwt secret";

    private Crypto crypto;
    private BiPredicate<String, Function<Long, Optional<User>>> validator;

    private BiFunction<Map<String, Object>, User, String> generator;

    @BeforeEach
    public void setUp() throws Exception {
        crypto = new AESCrypto("some secret");
        validator = new JWTValidator(SEC_VALID, JWT_SECRET, crypto);
        generator = new JWTGenerator(SEC_VALID, JWT_SECRET, crypto);
    }

    @Test
    public void testGeneratesValidToken() {
        // given
        final User user = TestUtil.generateUserWithoutTeam();

        //when
        final String token = generator.apply(new HashMap<>(), user);

        // then
        Assertions.assertTrue(validator.test(token, id -> Optional.of(user)));
    }

    @Test
    public void testExpiredTokenInvalid() {
        // given
        final User user = TestUtil.generateUserWithoutTeam();

        //when
        final String token = generator.apply(new HashMap<>(), user);

        // then
        Assertions.assertTrue(validator.test(token,id -> Optional.of(user)));

        try {
            Thread.sleep(SEC_VALID * Constants.MS_IN_SEC + 1);
        } catch (InterruptedException e) {
            //
        }

        Assertions.assertFalse(validator.test(token, id -> Optional.of(user)));

    }
}
