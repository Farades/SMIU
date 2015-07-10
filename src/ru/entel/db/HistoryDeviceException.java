package ru.entel.db;

import ru.entel.devices.DeviceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Farades on 24.06.2015.
 */
public class HistoryDeviceException {

    public static void saveStartDeviceException(DeviceException deviceException) {
        Connection dbConn = Database.getInstance().getConn();
        try {
            PreparedStatement stmt = dbConn.prepareStatement("INSERT INTO ALARM_LOG (data, time, state)  values (?, ?, ?)");
            stmt.setString(1, deviceException.getDescription());
            stmt.setString(2, deviceException.getTime_start());
            stmt.setString(3, "start");
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dbConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveEndDeviceException(DeviceException deviceException) {
        Connection dbConn = Database.getInstance().getConn();
        try {
            PreparedStatement stmt = dbConn.prepareStatement("INSERT INTO ALARM_LOG (data, time, state)  values (?, ?, ?)");
            stmt.setString(1, deviceException.getDescription());
            stmt.setString(2, deviceException.getTime_end());
            stmt.setString(3, "end");
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                dbConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<DeviceExceptionFromDb> getHistory() {
        Connection dbConn = Database.getInstance().getConn();
        ArrayList<DeviceExceptionFromDb> res = new ArrayList<DeviceExceptionFromDb>();
        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rst = stmt.executeQuery("SELECT * FROM ALARM_LOG ORDER BY id DESC");
            while (rst.next()) {
                String description = rst.getString("data");
                String time = rst.getString("time");
                String state = rst.getString("state");
                res.add(new DeviceExceptionFromDb(description, time, state));
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
        return res;
    }
}
