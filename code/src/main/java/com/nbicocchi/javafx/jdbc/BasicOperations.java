package com.nbicocchi.javafx.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

/**
 * A class for testing basic operations with JDBC.
 *
 * @author Nicola Bicocchi
 */
public class BasicOperations {
    HikariDataSource dataSource;

    public BasicOperations() throws SQLException {
        dbConnection();
        testDB();
        testSelect();
        testUpdate();
        testSelect();
        testScrollable();
        testUpdateable();
        testSelect();
        //testSensitive();
    }

    private void dbConnection() {
        System.out.println("- dbConnection()...");
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(UtilsDB.JDBC_Driver_MySQL);
        config.setJdbcUrl(UtilsDB.JDBC_URL_MySQL);
        config.setLeakDetectionThreshold(2000);
        dataSource = new HikariDataSource(config);
    }

    public void testDB() throws SQLException {
        System.out.println("- testDB()...");
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 1")) {
                ps.executeQuery();
            } catch (SQLException e) {
                try (Statement s = connection.createStatement()) {
                    s.addBatch("DROP TABLE IF EXISTS book");
                    s.addBatch("CREATE TABLE book (id INTEGER PRIMARY KEY, title VARCHAR(30), author VARCHAR(30), pages INTEGER)");
                    s.addBatch("INSERT INTO book (id, title, author, pages) VALUES(1, 'The Lord of the Rings', 'Tolkien', 241)");
                    s.addBatch("INSERT INTO book (id, title, author, pages) VALUES(2, 'Fight Club', 'Palahniuk', 212)");
                    s.addBatch("INSERT INTO book (id, title, author, pages) VALUES(3, 'Computer Networks', 'Tanenbaum', 313)");
                    s.addBatch("INSERT INTO book (id, title, author, pages) VALUES(4, 'Affective Computing', 'Picard', 127)");
                    s.executeBatch();
                }
            }
        }
    }

    /**
     * Reads the content of the person table Results are limited using "LIMIT 100"
     * Useful for large tables
     */
    public void testSelect() throws SQLException {
        System.out.println("- testSelect()...");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(rowToString(rs));
            }
        }
    }

    /**
     * Update the content of the book table
     */
    public void testUpdate() throws SQLException {
        System.out.println("- testUpdate()...");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE book SET pages=? WHERE id=?")) {
            ps.setInt(1, 176);
            ps.setInt(2, 1);
            ps.executeUpdate();
        }
    }

    /**
     * Test Scrollable ResultSet
     */
    public void testScrollable() throws SQLException {
        System.out.println("- testScrollable()...");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100 OFFSET 0",
             ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = ps.executeQuery();
            // Third record
            rs.absolute(2);
            System.out.println(rowToString(rs));
            // Previous record
            rs.previous();
            System.out.println(rowToString(rs));
            // +2 records from current position
            rs.relative(2);
            System.out.println(rowToString(rs));
        }
    }

    /**
     * Test Updateable ResultSet Increment pages of one element
     */
    public void testUpdateable() throws SQLException {
        System.out.println("- testUpdateable()...");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100 OFFSET 0", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int pages = rs.getInt("pages");
                rs.updateInt("pages", pages + 10);
                rs.updateRow();
            }
        }
    }

    /**
     * Test Sensitive ResultSet
     */
    public void testSensitive() throws SQLException {
        System.out.println("- testSensitive()...");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100 OFFSET 0", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = ps.executeQuery();
            for (int retry = 0; retry < 10; retry++) {
                System.out.printf("[%d] awaiting for external changes 10s...\n", retry);
                rs.beforeFirst();
                while (rs.next()) {
                    rs.refreshRow();
                    System.out.println(rowToString(rs));
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * Prints the current ResultSet row
     */
    public String rowToString(ResultSet rs) throws SQLException {
        return String.format("id=%d, title=%s, author=%s, pages=%d",
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("pages"));
    }

    public static void main(String[] args) {
        try {
            new BasicOperations();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
