package com.toptal.soccer.manager.config;

import com.toptal.soccer.manager.TransferManagerImpl;
import com.toptal.soccer.manager.iface.TransferManager;
import com.toptal.soccer.repo.TransferRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransferManagerConfig {
    
    @Bean
    public TransferManager transferManager(final TransferRepo transferRepo){
        return new TransferManagerImpl(transferRepo);
    }
}
