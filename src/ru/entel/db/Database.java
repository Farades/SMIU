package ru.entel.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Farades on 24.06.2015.
 */
public class Database {
    private static Connection conn;
    private static Database instance;
    private static DataSource ds;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
    }

    public static void setDataSource(DataSource ds) {
        Database.ds = ds;
    }

    public Connection getConn() {
        if (conn == null) {
            try {
                conn = ds.getConnection();
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
