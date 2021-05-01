package com.tsvika.home.ledger.reports.service.configuration;

import com.tsvika.home.ledger.common.EnvironmentVariablesWrapper;
import com.tsvika.home.ledger.common.IEnvironmentVariablesWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan( value = "com.tsvika.home.ledger.reports.service")
public class SpringConfiguration {

    @Bean
    public IEnvironmentVariablesWrapper environmentVariablesWrapper() {
        return new EnvironmentVariablesWrapper();
    }
}
