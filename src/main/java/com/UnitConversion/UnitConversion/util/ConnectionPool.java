package com.UnitConversion.UnitConversion.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionPool {

    public static Connection getConnection() {

        try {

            String url = ApplicationConfig.getProperty("db.url");
            String user = ApplicationConfig.getProperty("db.username");
            String password = ApplicationConfig.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }
}