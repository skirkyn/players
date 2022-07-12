package com.toptal.soccer.security.config;

import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.config.CryptoConfig;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.manager.config.UserManagerConfig;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.security.JWTRequestFilter;
import com.toptal.soccer.security.JWTValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Import({UserManagerConfig.class, CryptoConfig.class})
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTRequestFilter jwtRequestFilter;


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


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/user/login", "user/register").permitAll().
                anyRequest().authenticated().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}