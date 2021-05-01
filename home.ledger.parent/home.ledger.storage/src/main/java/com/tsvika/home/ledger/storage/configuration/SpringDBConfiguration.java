package com.tsvika.home.ledger.storage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/*
* SONAR
* SONAR
* */
@Configuration
public class SpringDBConfiguration {
    @Bean
    public DataSource dataSource() {
        return new DataBaseHelper().getDBDataSource();
    }
}
