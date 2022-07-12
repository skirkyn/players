package com.toptal.soccer.manager.config;

import com.toptal.soccer.manager.TeamManagerImpl;
import com.toptal.soccer.manager.iface.TeamManager;
import com.toptal.soccer.repo.TeamRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TeamManagerConfig {
    
    @Bean
    public TeamManager teamManager(final TeamRepo teamRepo){
        return new TeamManagerImpl(teamRepo);
    }
}
