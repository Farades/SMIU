package ru.entel.db;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Farades on 03.07.2015.
 */
public class LogSaverDB {
    public synchronized static ArrayList<LogRow> getDataLogsByDate(java.util.Date date) {
        ArrayList<LogRow> result = new ArrayList<LogRow>();
        Connection dbConn = Database.getInstance().getConn();
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sf.format(date);

            PreparedStatement stmt = dbConn.prepareStatement("SELECT * FROM DATA_LOG WHERE DATE_FORMAT(time, '%Y %m %d') = DATE_FORMAT(?, '%Y %m %d') ORDER BY id DESC");
            stmt.setString(1, dateStr);
            ResultSet rst = stmt.executeQuery();

            while (rst.next()) {
                Timestamp time = rst.getTimestamp("time");
                String data = rst.getString("data");
                String device = rst.getString("device");
                result.add(new LogRow(time, data, device));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dbConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public synchronized static ArrayList<LogRow> getDataLogsByCurrentDate() {
        ArrayList<LogRow> result = new ArrayList<LogRow>();
        Connection dbConn = Database.getInstance().getConn();
        try {
            java.util.Date date = new java.util.Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sf.format(date);

            PreparedStatement stmt = dbConn.prepareStatement("SELECT * FROM DATA_LOG WHERE DATE_FORMAT(time, '%Y %m %d') = DATE_FORMAT(?, '%Y %m %d') ORDER BY id DESC");
            stmt.setString(1, dateStr);
            ResultSet rst = stmt.executeQuery();

            while (rst.next()) {
                Timestamp time = rst.getTimestamp("time");
                String data = rst.getString("data");
                String device = rst.getString("device");
                result.add(new LogRow(time, data, device));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dbConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
