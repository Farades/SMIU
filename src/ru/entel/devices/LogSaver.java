package ru.entel.devices;

import ru.entel.db.Database;
import ru.entel.engine.Engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            saveLogs();
            try {
                Thread.sleep(this.timePause * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveLogs() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String current_time = dateFormat.format(new Date());
            Connection dbConn = Database.getInstance().getConn();
            for (Device device : engine.getDevices().values()) {
                PreparedStatement stmt = dbConn.prepareStatement("INSERT INTO DATA_LOG (time, data, device)  values (?, ?, ?)");
                stmt.setString(1, current_time);
                stmt.setString(2, device.toString());
                stmt.setString(3, device.getDescription());
                stmt.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.getInstance().closeConnection();
        }
    }

    public synchronized void stop() {
        this.running = false;
    }
}
