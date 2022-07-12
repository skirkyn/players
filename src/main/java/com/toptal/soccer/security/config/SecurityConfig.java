package com.toptal.soccer.security.config;

import com.toptal.soccer.crypto.config.CryptoConfig;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.manager.config.UserManagerConfig;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.security.JWTRequestFilter;
import com.toptal.soccer.security.JWTValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Import({UserManagerConfig.class, CryptoConfig.class})
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public JWTValidator jwtValidator(@Value("${soccer.security.jwt.token.sec.valid:3600}") final long secondsValid,
                                     @Value("${soccer.security.jwt.token.secret}") final String secret,
                                     final Crypto crypto) {
        return new JWTValidator(secondsValid, secret, crypto);
    }

    @Bean
    public JWTRequestFilter jwtRequestFilter(final UserManager userManager, final JWTValidator jwtValidator) {
        return new JWTRequestFilter(userManager, jwtValidator);
    }
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http, final JWTRequestFilter jwtRequestFilter) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/**").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();


        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}