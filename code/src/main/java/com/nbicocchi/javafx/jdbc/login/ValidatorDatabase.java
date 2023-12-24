package com.nbicocchi.javafx.jdbc.login;

import com.nbicocchi.javafx.jdbc.UtilsDB;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidatorDatabase implements Validator {
    private HikariDataSource dataSource;

    @Override
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(UtilsDB.JDBC_Driver_MySQL);
        config.setJdbcUrl(UtilsDB.JDBC_URL_MySQL);
        config.setLeakDetectionThreshold(2000);
        dataSource = new HikariDataSource(config);
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        try (Connection connection = dataSource.getConnection(); PreparedStatement getUsers = connection.prepareStatement("SELECT * FROM users WHERE username=?")) {
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

    public void resetDB() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement dropTable = connection.prepareStatement("DROP TABLE IF EXISTS users")) {
            dropTable.executeUpdate();
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement createTable = connection.prepareStatement("CREATE TABLE users (username VARCHAR(50) " + "PRIMARY KEY, password BIGINT)")) {
            createTable.executeUpdate();
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertPlane = connection.prepareStatement("INSERT INTO users (username, password) " + "VALUES (?, ?)")) {
            insertPlane.setString(1, "admin");
            insertPlane.setInt(2, "admin".hashCode());
            insertPlane.executeUpdate();
            insertPlane.setString(1, "user");
            insertPlane.setInt(2, "password".hashCode());
            insertPlane.executeUpdate();
        }
    }
}
