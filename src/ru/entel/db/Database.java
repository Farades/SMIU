package ru.entel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Farades on 24.06.2015.
 */
public class Database {
    private static Connection conn;
    private static Database instance;
    private static String dbURL = "jdbc:sqlite:C:/workspace/SMIUWEB/db/db.sqlite";

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(dbURL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = null;
    }
}
