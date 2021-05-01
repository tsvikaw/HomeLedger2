package com.tsvika.home.ledger.storage.configuration;

import com.tsvika.home.ledger.common.EnvironmentVariablesWrapper;
import com.tsvika.home.ledger.common.IEnvironmentVariablesWrapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EntityScan(value = "com.tsvika.home.ledger.storage.entities")
@EnableJpaRepositories(value = "com.tsvika.home.ledger.storage.repositories")
@ComponentScan( value = "com.tsvika.home.ledger.storage")
public class SpringConfiguration {

    @Bean
    public IEnvironmentVariablesWrapper environmentVariablesWrapper() {
        return new EnvironmentVariablesWrapper();
    }
}
