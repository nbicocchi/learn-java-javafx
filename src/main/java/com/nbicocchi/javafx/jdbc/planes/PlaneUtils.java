package com.nbicocchi.javafx.jdbc.planes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class PlaneUtils {
    public static void resetDB(Connection connection) throws SQLException {
        try (PreparedStatement dropTable = connection.prepareStatement("DROP TABLE IF EXISTS planes")) {
            dropTable.executeUpdate();
        }
        try (PreparedStatement createTable = connection.prepareStatement("CREATE TABLE planes (uuid VARCHAR(50) " + "PRIMARY KEY, name VARCHAR(50), length REAL, wingspan REAL, firstFlight DATE, category VARCHAR(50))")) {
            createTable.executeUpdate();
        }
    }

    public static List<Plane> loadFromDB(Connection connection) throws SQLException {
        List<Plane> planes = new ArrayList<>();
        try (PreparedStatement getPlanes = connection.prepareStatement("SELECT * FROM planes")) {
            try (ResultSet rs = getPlanes.executeQuery()) {
                while (rs.next()) {
                    planes.add(new Plane(UUID.fromString(rs.getString("uuid")), rs.getString("name"), rs.getDouble("length"), rs.getDouble("wingspan"), convertSQLDateToLocalDate(rs.getDate("firstFlight")), rs.getString("category")));
                }
            }
        }
        return planes;
    }

    public static LocalDate convertSQLDateToLocalDate(Date SQLDate) {
        java.util.Date date = new java.util.Date(SQLDate.getTime());
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static void saveToDB(List<Plane> planes, Connection connection) throws SQLException {
        try (PreparedStatement insertPlane = connection.prepareStatement("INSERT INTO planes (uuid, name, length, wingspan, firstFlight, category) VALUES (?, ?, ?, ?, ?, ?)")) {
            for (Plane plane : planes) {
                insertPlane.setString(1, plane.getUUID().toString());
                insertPlane.setString(2, plane.getName());
                insertPlane.setDouble(3, plane.getLength());
                insertPlane.setDouble(4, plane.getWingspan());
                insertPlane.setDate(5, Date.valueOf(plane.getFirstFlight()));
                insertPlane.setString(6, plane.getCategory());
                insertPlane.executeUpdate();
            }
        }
    }

    public static List<Plane> loadFromFile(Path path) throws IOException {
        List<Plane> planes = new ArrayList<>();
        try (Scanner scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                String[] fields = scanner.nextLine().split(";");
                planes.add(new Plane(fields[0], Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), LocalDate.parse(fields[3]), fields[4]));
            }
        }
        return planes;
    }

    public static void saveToFile(List<Plane> planes, Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Plane plane : planes) {
            lines.add(plane.toCSV());
        }
        Files.write(path, lines);
    }
}
