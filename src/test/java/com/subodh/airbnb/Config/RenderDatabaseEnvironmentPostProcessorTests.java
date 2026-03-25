package com.subodh.airbnb.Config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RenderDatabaseEnvironmentPostProcessorTests {

    private final RenderDatabaseEnvironmentPostProcessor postProcessor =
            new RenderDatabaseEnvironmentPostProcessor();

    @Test
    void convertsRenderConnectionStringIntoJdbcDatasourceProperties() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(
                "DATABASE_URL",
                "postgresql://render_user:render_pass@db.internal:5432/airbnb_db"
        );

        postProcessor.postProcessEnvironment(environment, new SpringApplication());

        assertEquals(
                "jdbc:postgresql://db.internal:5432/airbnb_db",
                environment.getProperty("spring.datasource.url")
        );
        assertEquals("render_user", environment.getProperty("spring.datasource.username"));
        assertEquals("render_pass", environment.getProperty("spring.datasource.password"));
    }

    @Test
    void keepsExistingJdbcDatasourceUrlUntouched() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("spring.datasource.url", "jdbc:postgresql://existing-host:5432/existing_db");
        environment.setProperty(
                "DATABASE_URL",
                "postgresql://render_user:render_pass@db.internal:5432/airbnb_db"
        );

        postProcessor.postProcessEnvironment(environment, new SpringApplication());

        assertEquals(
                "jdbc:postgresql://existing-host:5432/existing_db",
                environment.getProperty("spring.datasource.url")
        );
    }
}
