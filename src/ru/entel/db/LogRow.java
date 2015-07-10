package ru.entel.db;

import java.sql.Timestamp;

/**
 * Created by Farades on 03.07.2015.
 */
public class LogRow {
    private Timestamp time;
    private String data;
    private String device;

    public LogRow(Timestamp time, String data, String device) {
        this.time = time;
        this.data = data;
        this.device = device;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getData() {
        return data;
    }

    public String getDevice() {
        return device;
    }
}
