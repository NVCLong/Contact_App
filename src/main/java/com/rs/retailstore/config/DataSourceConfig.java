package com.rs.retailstore.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder= DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/retail_store");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("longdsaewq");
        return dataSourceBuilder.build();
    }
}
