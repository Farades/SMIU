package ru.entel.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Farades on 24.06.2015.
 */
public class Database {
    private static Database instance;
    private static DataSource ds;

    public synchronized static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
    }

    public synchronized static void setDataSource(DataSource ds) {
        if (Database.ds == null) {
            Database.ds = ds;
        }
    }

    public synchronized Connection getConn() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
