package com.tsvika.home.ledger.editing.service.configuration;
import com.tsvika.home.ledger.common.EnvironmentVariablesWrapper;
import com.tsvika.home.ledger.common.IEnvironmentVariablesWrapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan( value = "com.tsvika.home.ledger.editing")
public class SpringConfiguration {

    @Bean
    public IEnvironmentVariablesWrapper environmentVariablesWrapper() {
        return new EnvironmentVariablesWrapper();
    }
}
