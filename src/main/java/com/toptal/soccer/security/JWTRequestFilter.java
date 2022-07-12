package com.toptal.soccer.security;

import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class JWTRequestFilter extends OncePerRequestFilter {

    private final UserManager userManager;
    private final BiPredicate<String, Function<Long, Optional<User>>> jwtValidator;

    public JWTRequestFilter(UserManager userManager, BiPredicate<String, Function<Long, Optional<User>>> jwtValidator) {
        this.userManager = userManager;
        this.jwtValidator = jwtValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestTokenHeader != null && requestTokenHeader.startsWith(Constants.BEARER)) {

            final String jwtToken = StringUtils.substringAfter(requestTokenHeader, Constants.BEARER);

            if (!jwtValidator.test(jwtToken, id -> userManager.findById(id)
                    .stream().peek(u ->
                        SecurityContextHolder.getContext()
                                .setAuthentication(
                                        new PreAuthenticatedAuthenticationToken(u.getId(), jwtToken))

                    ).findFirst())) {
                throw new SecurityException(Constants.JWT_TOKEN_IS_INVALID);
            }
        }
    }
}
