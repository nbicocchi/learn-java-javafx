package com.nbicocchi.javafx.basic;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for testing basic operations with JDBC.
 *
 * @author Nicola Bicocchi
 */
public class App {
    private final static Logger LOG = LoggerFactory.getLogger(App.class);
    HikariDataSource dataSource;

    private App() {
        dbConnection();
        resetDB();
        testSelect();
        testUpdate();
        testSelect();
        testScrollable();
        //testUpdateable();
        //testSelect();
        testUpdateWithTransactions();
        testSelect();
    }

    /**
     * Creates a pool of connections to the DB
     */
    private void dbConnection() {
        LOG.info("** dbConnection() **");
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(UtilsDB.JDBC_Driver_PostgreSQL);
        config.setJdbcUrl(UtilsDB.JDBC_URL_PostgreSQL);
        config.setLeakDetectionThreshold(2000);
        dataSource = new HikariDataSource(config);
    }

    /**
     * Creates a table for books filled with some example data.
     * OK only for debug/dev purposes. Not to use in production!
     */
    private void resetDB() {
        LOG.info("** resetDB() **");
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
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
     * Reads the content of the book table limiting results to 100
     * Useful for large tables
     */
    private void testSelect() {
        LOG.info("** testSelect() **");
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LOG.info(rowToString(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Update the content of one record of the book table
     */
    private void testUpdate() {
        LOG.info("** testUpdate() **");
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement("UPDATE book SET pages=? WHERE id=?")) {
            ps.setInt(1, 333);
            ps.setInt(2, 1);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test Scrollable ResultSet over the book table
     */
    private void testScrollable() {
        LOG.info("** testScrollable() **");
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = ps.executeQuery();
            // Third record
            rs.absolute(2);
            LOG.info(rowToString(rs));
            // Previous record
            rs.previous();
            LOG.info(rowToString(rs));
            // +2 records from current position
            rs.relative(2);
            LOG.info(rowToString(rs));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Transform the current ResultSet row into a String
     */
    private String rowToString(ResultSet rs) throws SQLException {
        return String.format("--> id=%d, title=%s, author=%s, pages=%d", rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getInt("pages"));
    }

    /**
     * Test Updateable ResultSet over the book table
     */
    private void testUpdateable() {
        LOG.info("** testUpdateable() **");
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); ResultSet rs = ps.executeQuery()) {
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
     * Test a simple transaction involving changes to two different books.
     */
    private void testUpdateWithTransactions() {
        LOG.info("** testUpdateWithTransactions() **");
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement("UPDATE book SET pages=? WHERE id=?")
        ) {
            // disable auto-commit
            connection.setAutoCommit(false);

            // the first book has 100 pages
            ps.setInt(1, 100);
            ps.setInt(2, 1);
            ps.executeUpdate();

            // the second book has 200 pages
            ps.setInt(1, 200);
            ps.setInt(2, 2);
            ps.executeUpdate();

            // all changes are actually committed together
            connection.commit();

            // re-enable auto-commit
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Test Sensitive ResultSet over the book table
     */
    private void testSensitive() {
        LOG.info("** testSensitive() **");
        try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement("SELECT * FROM book LIMIT 100 OFFSET 0", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = ps.executeQuery();
            for (int retry = 0; retry < 10; retry++) {
                System.out.printf("[%d] awaiting for external changes 10s...\n", retry);
                rs.beforeFirst();
                while (rs.next()) {
                    rs.refreshRow();
                    LOG.info(rowToString(rs));
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

    public static void main(String[] args) {
        new App();
    }
}
