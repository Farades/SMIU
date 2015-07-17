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
            PreparedStatement stmt = dbConn.prepareStatement("INSERT INTO alarm_log (data, time, state, device, value)  values (?, ?, ?, ?, ?)");
            stmt.setString(1, deviceException.getDescription());
            stmt.setString(2, deviceException.getTime_start());
            stmt.setString(3, "start");
            stmt.setString(4, deviceException.getDeviceOwner());
            stmt.setString(5, deviceException.getCurrentValue().toString());
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
            PreparedStatement stmt = dbConn.prepareStatement("INSERT INTO alarm_log (data, time, state, device, value)  values (?, ?, ?, ?, ?)");
            stmt.setString(1, deviceException.getDescription());
            stmt.setString(2, deviceException.getTime_end());
            stmt.setString(3, "end");
            stmt.setString(4, deviceException.getDeviceOwner());
            stmt.setString(5, deviceException.getCurrentValue().toString());
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

    public static int getAlarmSize() {
        int res = 0;
        Connection dbConn = Database.getInstance().getConn();
        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rst = stmt.executeQuery("SELECT COUNT(*) FROM alarm_log");
            while (rst.next()) {
                res = rst.getInt(1);
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

    public static ArrayList<DeviceExceptionFromDb> getHistory(int first, int pageSize) {
        Connection dbConn = Database.getInstance().getConn();
        ArrayList<DeviceExceptionFromDb> res = new ArrayList<DeviceExceptionFromDb>();
        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rst = stmt.executeQuery("SELECT * FROM alarm_log ORDER BY id DESC LIMIT " + first + ", " + pageSize);
            while (rst.next()) {
                String description = rst.getString("data");
                String time = rst.getString("time");
                String state = rst.getString("state");
                String device = rst.getString("device");
                String value = rst.getString("value");
                res.add(new DeviceExceptionFromDb(description, time, state, device, value));
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
