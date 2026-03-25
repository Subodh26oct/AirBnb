package com.subodh.airbnb.Config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RenderDatabaseEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String currentDatasourceUrl = environment.getProperty("spring.datasource.url");
        if (hasText(currentDatasourceUrl) && currentDatasourceUrl.startsWith("jdbc:")) {
            return;
        }

        String databaseUrl = firstNonBlank(
                environment.getProperty("DATABASE_URL"),
                environment.getProperty("DB_URL")
        );

        if (!hasText(databaseUrl)) {
            return;
        }

        if (databaseUrl.startsWith("jdbc:")) {
            Map<String, Object> properties = new HashMap<>();
            properties.put("spring.datasource.url", databaseUrl);
            environment.getPropertySources().addFirst(
                    new MapPropertySource("renderJdbcDatasource", properties)
            );
            return;
        }

        if (!databaseUrl.startsWith("postgresql://")) {
            return;
        }

        URI uri = URI.create(databaseUrl);
        String host = uri.getHost();
        String databaseName = trimLeadingSlash(uri.getPath());
        int port = uri.getPort() > 0 ? uri.getPort() : 5432;

        if (!hasText(host) || !hasText(databaseName)) {
            return;
        }

        String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
        if (hasText(uri.getQuery())) {
            jdbcUrl += "?" + uri.getQuery();
        }

        String username = firstNonBlank(
                environment.getProperty("spring.datasource.username"),
                environment.getProperty("DB_USERNAME"),
                userInfoPart(uri.getUserInfo(), 0)
        );

        String password = firstNonBlank(
                environment.getProperty("spring.datasource.password"),
                environment.getProperty("DB_PASSWORD"),
                userInfoPart(uri.getUserInfo(), 1)
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("spring.datasource.url", jdbcUrl);
        if (hasText(username)) {
            properties.put("spring.datasource.username", username);
        }
        if (hasText(password)) {
            properties.put("spring.datasource.password", password);
        }

        environment.getPropertySources().addFirst(
                new MapPropertySource("renderPostgresDatasource", properties)
        );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static String trimLeadingSlash(String value) {
        if (!hasText(value)) {
            return value;
        }
        return value.startsWith("/") ? value.substring(1) : value;
    }

    private static String userInfoPart(String userInfo, int index) {
        if (!hasText(userInfo)) {
            return null;
        }
        String[] parts = userInfo.split(":", 2);
        return parts.length > index ? parts[index] : null;
    }
}
