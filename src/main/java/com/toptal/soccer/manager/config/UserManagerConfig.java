package com.toptal.soccer.manager.config;

import com.toptal.soccer.manager.UserManagerImpl;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.repo.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserManagerConfig {
    
    @Bean
    public UserManager userManager(final UserRepo userRepo){
        return new UserManagerImpl(userRepo);
    }
}
