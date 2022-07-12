package com.toptal.soccer.security;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

@Slf4j
public class JWTValidator implements  BiPredicate<String, Function<Long, Optional<User>>> {

    private final long secondsValid;
    private final String secret;
    private final Crypto crypto;

    public JWTValidator(final long secondsValid, final String secret, final Crypto crypto) {
        this.secondsValid = secondsValid;
        this.secret = secret;
        this.crypto = crypto;
    }

    @Override
    public boolean test(final String token, final Function<Long, Optional<User>> userFunction) {
        Validate.notEmpty(token);
        Validate.notNull(userFunction);

        try {
            final Claims claims = getAllClaimsFromToken(token);
            final Long userId = Long.valueOf(crypto.decrypt(claims.getSubject()));
            final User user = userFunction.apply(userId).orElseThrow(
                    () -> new IllegalArgumentException(Constants.USER_DOESN_T_EXIST));
            final Date expiration = claims.getExpiration();

            return Objects.equals(user.getId(), userId) && expiration.after(new Date());

        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }

    }

    private Claims getAllClaimsFromToken(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

    }
}
