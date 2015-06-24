package ru.entel.db;

import ru.entel.devices.DeviceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Farades on 24.06.2015.
 */
public class HistoryDeviceException {

    public static void saveDeviceException(DeviceException deviceException) {
        Connection dbConn = Database.getInstance().getConn();
        try {
            PreparedStatement stmt = dbConn.prepareStatement("INSERT INTO exception ('device', 'description', 'time_start', 'time_end')" +
                    "values (?, ?, ?, ?)");
            stmt.setString(1, deviceException.getDeviceOwner());
            stmt.setString(2, deviceException.getDescription());
            stmt.setString(3, deviceException.getTime_start());
            stmt.setString(4, deviceException.getTime_end());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection();
        }
    }

    public static ArrayList<DeviceExceptionFromDb> getHistory() {
        Connection dbConn = Database.getInstance().getConn();
        ArrayList<DeviceExceptionFromDb> res = new ArrayList<DeviceExceptionFromDb>();
        try {
            Statement stmt = dbConn.createStatement();
            ResultSet rst = stmt.executeQuery("SELECT * FROM `exception` ORDER BY `id` DESC");
            while (rst.next()) {
                String device = rst.getString("device");
                String description = rst.getString("description");
                String time_start = rst.getString("time_start");
                String time_end = rst.getString("time_end");
                res.add(new DeviceExceptionFromDb(device, description, time_start, time_end));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return res;
    }
}
