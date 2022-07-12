package com.toptal.soccer.crypto.config;

import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoConfig {
    @Bean
    public Crypto jwtCrypto(@Value("${soccer.crypto.secret}") final String secret,
                            @Value("${soccer.crypto.transformation}") final String transformation,
                            @Value("${soccer.crypto.key.hashing.algorithm=}") final String hashingAlgorithm) throws Exception {
        return new AESCrypto(secret, transformation, hashingAlgorithm);
    }
}
