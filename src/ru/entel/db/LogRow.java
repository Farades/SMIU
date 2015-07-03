package ru.entel.db;

/**
 * Created by Farades on 03.07.2015.
 */
public class LogRow {
    private String time;
    private String data;
    private String device;

    public LogRow(String time, String data, String device) {
        this.time = time;
        this.data = data;
        this.device = device;
    }

    public String getTime() {
        return time;
    }

    public String getData() {
        return data;
    }

    public String getDevice() {
        return device;
    }
}
