package com.bank.transfer.configuration;

import com.bank.transfer.context.NotificationContext;
import com.bank.transfer.repositories.AccountRepository;
import com.bank.transfer.services.AccountApplicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class DependencyInjectionConfig {

    public DependencyInjectionConfig() {
    }

    @Bean
    @RequestScope
    public NotificationContext notificationContextConfiguration() {
        return new NotificationContext();
    }

    @Bean
    @RequestScope
    public AccountApplicationService accountApplicationServiceConfiguration(
            @Autowired AccountRepository accountRepository, @Autowired NotificationContext notificationContext) {
        return new AccountApplicationService(accountRepository, notificationContext);
    }
}
