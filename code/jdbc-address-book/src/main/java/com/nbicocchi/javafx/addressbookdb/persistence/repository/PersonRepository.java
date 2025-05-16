package com.nbicocchi.javafx.addressbookdb.persistence.repository;

import com.nbicocchi.javafx.addressbookdb.persistence.model.Person;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PersonRepository implements Repository<Person, Long> {
    private final HikariDataSource dataSource;

    public PersonRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        checkTable();
    }

    private void checkTable() {
        String sql = "SELECT * FROM persons LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
        } catch (SQLException e) {
            // Must be disabled in production!
            initTable();
        }
    }

    private void initTable() {
        String sql = "DROP TABLE IF EXISTS persons;" +
                "CREATE TABLE persons " +
                "(id SERIAL, " +
                "firstName VARCHAR(50) DEFAULT NULL, " +
                "lastName VARCHAR(50) DEFAULT NULL, " +
                "street VARCHAR(50) DEFAULT NULL, " +
                "postalCode INTEGER DEFAULT NULL, " +
                "city VARCHAR(50) DEFAULT NULL, " +
                "birthDay DATE DEFAULT NULL, " +
                "PRIMARY KEY (id))";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Person> findById(Long Id) {
        String sql = "SELECT * FROM persons WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, Id);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new Person(
                    rs.getLong("id"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("street"),
                    rs.getInt("postalCode"),
                    rs.getString("city"),
                    rs.getDate("birthDay").toLocalDate()));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Iterable<Person> findAll() {
        String sql = "SELECT * FROM persons";
        List<Person> persons = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                persons.add(new Person(
                        rs.getLong("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("street"),
                        rs.getInt("postalCode"),
                        rs.getString("city"),
                        rs.getDate("birthDay").toLocalDate()));
            }
            return persons;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Person save(Person entity) {
        // entity does not have an id
        if (Objects.isNull(entity.getId())) {
            return insert(entity);
        }

        Optional<Person> plane = findById(entity.getId());
        if (plane.isEmpty()) {
            // entity has an id but does not exist on the db (no bueno!)
            return insert(entity);
        } else {
            // entity has an id which is present on the db
            return update(entity);
        }
    }

    private Person insert(Person entity) {
        String sql = "INSERT INTO persons (firstName, lastName, street, postalCode, city, birthDay) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getStreet());
            statement.setInt(4, entity.getPostalCode());
            statement.setString(5, entity.getCity());
            statement.setDate(6, Date.valueOf(entity.getBirthday()));
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

    private Person update(Person entity) {
        String sql = "UPDATE persons SET firstName=?, lastName=?, street=?, postalCode=?, city=?, birthDay=? WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getStreet());
            statement.setInt(4, entity.getPostalCode());
            statement.setString(5, entity.getCity());
            statement.setDate(6, Date.valueOf(entity.getBirthday()));
            statement.setLong(7, entity.getId());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Person entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteById(Long Id) {
        String sql = "DELETE FROM persons WHERE id=?";
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
        String sql = "DELETE FROM persons";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
