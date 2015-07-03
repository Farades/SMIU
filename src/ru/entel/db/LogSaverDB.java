package ru.entel.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Farades on 03.07.2015.
 */
public class LogSaverDB {
    public static ArrayList<LogRow> getDataLogs() {
        ArrayList<LogRow> result = new ArrayList<LogRow>();
        Connection dbConn = Database.getInstance().getConn();
        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rst = stmt.executeQuery("SELECT * FROM DATA_LOG ORDER BY id DESC");
            while (rst.next()) {
                String time = rst.getString("time");
                String data = rst.getString("data");
                String device = rst.getString("device");
                result.add(new LogRow(time, data, device));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.getInstance().closeConnection();
        }
        return result;
    }
}
