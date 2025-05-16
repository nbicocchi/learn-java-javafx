package com.nbicocchi.javafx.planes.persistence.dao;

import com.nbicocchi.javafx.planes.persistence.model.Plane;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlaneRepository implements Repository<Plane, Long> {
    private final HikariDataSource dataSource;

    public PlaneRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        checkTable();
    }

    private void checkTable() {
        String sql = "SELECT * FROM planes LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
        } catch (SQLException e) {
            // Must be disabled in production!
            initTable();
        }
    }

    private void initTable() {
        String sql = "DROP TABLE IF EXISTS planes;" +
                "CREATE TABLE planes " +
                "(id SERIAL, " +
                "name VARCHAR(50) DEFAULT NULL, " +
                "length DOUBLE PRECISION DEFAULT NULL, " +
                "wingspan DOUBLE PRECISION DEFAULT NULL, " +
                "firstFlight DATE DEFAULT NULL, " +
                "category VARCHAR(50) DEFAULT NULL, " +
                "PRIMARY KEY (id))";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Plane> findById(Long Id) {
        String sql = "SELECT * FROM planes WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, Id);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new Plane(rs.getLong("id"), rs.getString("name"), rs.getDouble("length"), rs.getDouble("wingspan"), rs.getDate("firstFlight").toLocalDate(), rs.getString("category")));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Iterable<Plane> findAll() {
        String sql = "SELECT * FROM planes";
        List<Plane> planeList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                planeList.add(new Plane(rs.getLong("id"), rs.getString("name"), rs.getDouble("length"), rs.getDouble("wingspan"), rs.getDate("firstFlight").toLocalDate(), rs.getString("category")));
            }
            return planeList;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Plane save(Plane entity) {
        if (Objects.isNull(entity.getId())) {
            return insert(entity);
        }

        Optional<Plane> plane = findById(entity.getId());
        if (plane.isEmpty()) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    private Plane insert(Plane entity) {
        String sql = "INSERT INTO planes (name, length, wingspan, firstFlight, category) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setDouble(2, entity.getLength());
            statement.setDouble(3, entity.getWingspan());
            statement.setDate(4, Date.valueOf(entity.getFirstFlight()));
            statement.setString(5, entity.getCategory());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                entity.setId(keys.getLong(1));
                return entity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Plane update(Plane entity) {
        String sql = "UPDATE planes SET name=?, length=?, wingspan=?, firstFlight=?, category=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setDouble(2, entity.getLength());
            statement.setDouble(3, entity.getWingspan());
            statement.setDate(4, Date.valueOf(entity.getFirstFlight()));
            statement.setString(5, entity.getCategory());
            statement.setLong(6, entity.getId());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Plane entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteById(Long Id) {
        String sql = "DELETE FROM planes WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, Id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM planes";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
