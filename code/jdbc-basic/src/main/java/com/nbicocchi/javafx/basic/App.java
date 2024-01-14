package com.nbicocchi.javafx.basic;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

/**
 * A class for testing basic operations with JDBC.
 *
 * @author Nicola Bicocchi
 */
public class App {
    HikariDataSource dataSource;

    private App() {
        dbConnection();
        resetDB();
        testSelect();
        testUpdate();
        testSelect();
        testScrollable();
        testSelect();
        //testUpdateable();
        //testSelect();
        //testSensitive();
    }

    private void dbConnection() {
        System.out.println("- dbConnection()...");
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(UtilsDB.JDBC_Driver_PostgreSQL);
        config.setJdbcUrl(UtilsDB.JDBC_URL_PostgreSQL);
        config.setLeakDetectionThreshold(2000);
        dataSource = new HikariDataSource(config);
    }

    private void resetDB() {
        System.out.println("- testDB()...");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.addBatch("DROP TABLE IF EXISTS book");
            statement.addBatch("CREATE TABLE book (id INTEGER PRIMARY KEY, title VARCHAR(30), author VARCHAR(30), pages INTEGER)");
            statement.addBatch("INSERT INTO book (id, title, author, pages) VALUES(1, 'The Lord of the Rings', 'Tolkien', 241)");
            statement.addBatch("INSERT INTO book (id, title, author, pages) VALUES(2, 'Fight Club', 'Palahniuk', 212)");
            statement.addBatch("INSERT INTO book (id, title, author, pages) VALUES(3, 'Computer Networks', 'Tanenbaum', 313)");
            statement.addBatch("INSERT INTO book (id, title, author, pages) VALUES(4, 'Affective Computing', 'Picard', 127)");
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Reads the content of the person table Results are limited using "LIMIT 100"
     * Useful for large tables
     */
    private void testSelect() {
        System.out.println("- testSelect()...");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(rowToString(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Update the content of the book table
     */
    private void testUpdate() {
        System.out.println("- testUpdate()...");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE book SET pages=? WHERE id=?")) {
            ps.setInt(1, 333);
            ps.setInt(2, 1);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test Scrollable ResultSet
     */
    private void testScrollable() {
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
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test Updateable ResultSet Increment pages of one element
     */
    private void testUpdateable() {
        System.out.println("- testUpdateable()...");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100 OFFSET 0", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int pages = rs.getInt("pages");
                rs.updateInt("pages", pages + 10);
                rs.updateRow();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test Sensitive ResultSet
     */
    private void testSensitive() {
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
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Prints the current ResultSet row
     */
    private String rowToString(ResultSet rs) throws SQLException {
        return String.format("id=%d, title=%s, author=%s, pages=%d",
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("pages"));
    }

    public static void main(String[] args) {
        new App();
    }
}
