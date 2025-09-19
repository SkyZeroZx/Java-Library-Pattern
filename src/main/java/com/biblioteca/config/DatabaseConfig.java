package com.biblioteca.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@Configuration
public class DatabaseConfig extends AbstractR2dbcConfiguration {
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return DatabaseConnection.getInstance().getConnectionFactory();
    }
}

// Patron Singleton para la conexion a la base de datos
class DatabaseConnection {
    private static DatabaseConnection instance;
    private final ConnectionFactory connectionFactory;

    private DatabaseConnection() {
        this.connectionFactory = ConnectionFactoryBuilder
            .withUrl("r2dbc:h2:mem:///biblioteca")
            .username("sa")
            .password("")
            .build();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }
}
