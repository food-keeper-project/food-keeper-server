package com.foodkeeper.foodkeeperserver.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@Configurable
public class DBConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.first")
    public DataSource firstDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
