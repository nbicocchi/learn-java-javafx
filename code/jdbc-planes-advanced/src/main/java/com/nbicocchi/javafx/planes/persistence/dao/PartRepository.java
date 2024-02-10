package com.nbicocchi.javafx.planes.persistence.dao;

import com.nbicocchi.javafx.planes.persistence.model.Part;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PartRepository implements Repository<Part, Long> {
    private static final Logger LOG = LoggerFactory.getLogger(PartRepository.class);
    private final HikariDataSource dataSource;

    public PartRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        checkTable();
    }

    private void checkTable() {
        LOG.info("Checking table [PARTS]");
        String sql = "SELECT * FROM parts LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeQuery();
        } catch (SQLException e) {
            // Must be disabled in production!
            initTable();
        }
    }

    private void initTable() {
        LOG.info("Initializing table [PARTS]");
        String sql = "DROP TABLE IF EXISTS parts;" +
                "CREATE TABLE parts " +
                "(id SERIAL, " +
                "planeid BIGINT DEFAULT NULL, " +
                "partcode VARCHAR(50) DEFAULT NULL, " +
                "description VARCHAR(50) DEFAULT NULL, " +
                "duration DOUBLE PRECISION DEFAULT NULL, " +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY (planeid) REFERENCES planes(id))";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Part> findById(Long Id) {
        LOG.info("Executing findByID() [PARTS]");
        String sql = "SELECT * FROM parts WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, Id);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new Part(
                    rs.getLong("id"),
                    rs.getLong("planeid"),
                    rs.getString("partcode"),
                    rs.getString("description"),
                    rs.getDouble("duration")));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Iterable<Part> findAll() {
        LOG.info("Executing findAll() [PARTS]");
        String sql = "SELECT * FROM parts";
        List<Part> partList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                partList.add(new Part(
                        rs.getLong("id"),
                        rs.getLong("planeid"),
                        rs.getString("partcode"),
                        rs.getString("description"),
                        rs.getDouble("duration")));
            }
            return partList;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Part save(Part entity) {
        LOG.info("Executing save() [PARTS]");
        if (Objects.isNull(entity.getId())) {
            return insert(entity);
        }

        Optional<Part> part = findById(entity.getId());
        if (part.isEmpty()) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    private Part insert(Part entity) {
        LOG.info("Executing insert() [PARTS]");
        String sql = "INSERT INTO parts (planeid, partcode, description, duration) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getPlaneID());
            statement.setString(2, entity.getPartCode());
            statement.setString(3, entity.getDescription());
            statement.setDouble(4, entity.getDuration());
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

    private Part update(Part entity) {
        LOG.info("Executing update() [PARTS]");
        String sql = "UPDATE parts SET partcode=?, description=?, duration=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getPartCode());
            statement.setString(2, entity.getDescription());
            statement.setDouble(3, entity.getDuration());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Part entity) {
        LOG.info("Executing delete() [PARTS]");
        deleteById(entity.getId());
    }

    @Override
    public void deleteById(Long Id) {
        LOG.info("Executing deleteById() [PARTS]");
        String sql = "DELETE FROM parts WHERE id=?";
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
        LOG.info("Executing deleAll() [PARTS]");
        String sql = "DELETE FROM parts";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
