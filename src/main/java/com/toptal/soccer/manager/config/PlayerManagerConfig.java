package com.toptal.soccer.manager.config;

import com.toptal.soccer.manager.PlayerManagerImpl;
import com.toptal.soccer.manager.iface.PlayerManager;
import com.toptal.soccer.repo.PlayerRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlayerManagerConfig {

    @Bean
    public PlayerManager playerManager(final PlayerRepo playerRepo){
        return new PlayerManagerImpl(playerRepo);
    }
}
