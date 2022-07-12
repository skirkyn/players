package com.toptal.soccer.security;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.Validate;

import java.util.Date;
import java.util.Map;
import java.util.function.BiFunction;

public class JWTGenerator implements BiFunction<Map<String, Object>, User, String> {
    private final long secondsValid;
    private final String secret;
    private final Crypto crypto;

    public JWTGenerator(final long secondsValid, final String secret, final Crypto crypto) {
        this.secondsValid = secondsValid;
        this.secret = secret;
        this.crypto = crypto;
    }

    @Override
    public String apply(Map<String, Object> claims, User user) {

        Validate.notNull(claims, Constants.CLAIMS_CANT_BE_NULL);
        Validate.notNull(user, Constants.USER_CANT_BE_NULL);

        return Jwts.builder().setClaims(claims)
                .setSubject(crypto.encrypt(user.getId().toString()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + secondsValid * Constants.MS_IN_SEC))
                .signWith(SignatureAlgorithm.HS512, secret).
                compact();
    }
}
