package ru.entel.devices;

import ru.entel.db.Database;
import ru.entel.engine.Engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Farades on 03.07.2015.
 */
public class LogSaver implements Runnable {
    private Engine engine;
    private int timePause;
    private volatile boolean running = true;

    /**
     *
     * @param engine
     * @param timePause Частота сохранения логов в секундах
     */
    public LogSaver(Engine engine, int timePause) {
        this.engine = engine;
        this.timePause = timePause;
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                Thread.sleep(this.timePause * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            saveLogs();
        }
    }

    private synchronized void saveLogs() {
        Connection dbConn = Database.getInstance().getConn();
        try {
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            for (Device device : engine.getDevices().values()) {
                PreparedStatement stmt = dbConn.prepareStatement("INSERT INTO DATA_LOG (time, data, device)  values (?, ?, ?)");
                stmt.setTimestamp(1, timestamp);
                stmt.setString(2, device.toString());
                stmt.setString(3, device.getDescription());
                stmt.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                dbConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void stop() {
        this.running = false;
    }
}
