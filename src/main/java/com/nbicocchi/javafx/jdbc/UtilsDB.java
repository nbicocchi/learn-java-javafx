package com.nbicocchi.javafx.jdbc;

import java.nio.ByteBuffer;
import java.util.TimeZone;
import java.util.UUID;

public class UtilsDB {
    public static final String JDBC_Driver_H2 = "org.h2.Driver";
    public static final String JDBC_URL_H2_Persistent = String.format("jdbc:h2:%s", getDesktopDir(), "OOPCourse.h2");
    public static final String JDBC_URL_H2_Memory = "jdbc:h2:mem:test";
    public static final String JDBC_Driver_SQLite = "org.sqlite.JDBC";
    public static final String JDBC_URL_SQLite = String.format("jdbc:sqlite:%s", getDesktopDir(), "OOPCourse.sqlite");
    public static final String JDBC_Driver_MySQL = "com.mysql.cj.jdbc.Driver";
    public static final String JDBC_URL_MySQL = "jdbc:mysql://localhost:3306/jdbc_schema?user=nicola&password=qwertyuio&serverTimezone=" + TimeZone.getDefault().getID();

    public static String getDesktopDir() {
        return String.format("%s%s%s", System.getProperty("user.home"), System.getProperty("file.separator"),
                "Desktop");
    }

    public static UUID asUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    public static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
