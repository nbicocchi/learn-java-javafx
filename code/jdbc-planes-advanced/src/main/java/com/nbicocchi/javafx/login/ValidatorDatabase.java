package com.nbicocchi.javafx.login;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class ValidatorDatabase implements Validator {
    private static final String JDBC_Driver = "org.postgresql.Driver";
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/jdbc_schema?user=user&password=secret&ssl=false";
    private HikariDataSource dataSource;

    @Override
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(JDBC_Driver);
        config.setJdbcUrl(JDBC_URL);
        config.setLeakDetectionThreshold(2000);
        dataSource = new HikariDataSource(config);
        // disable in production!
        resetDB();
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement getUsers = connection.prepareStatement(sql)) {
            getUsers.setString(1, username);
            ResultSet rs = getUsers.executeQuery();
            if (!rs.next()) {
                return false;
            }
            return password.hashCode() == rs.getLong("password");
        } catch (SQLException e) {
            return false;
        }
    }

    public void resetDB() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.addBatch("DROP TABLE IF EXISTS users");
            statement.addBatch("CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(20) UNIQUE, password BIGINT)");
            statement.addBatch(String.format("INSERT INTO users (username, password) VALUES (%s, %d)", "\'admin\'", "password".hashCode()));
            statement.addBatch(String.format("INSERT INTO users (username, password) VALUES (%s, %d)", "\'user\'", "secret".hashCode()));
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
