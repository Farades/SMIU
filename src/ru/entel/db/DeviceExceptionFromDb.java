package ru.entel.db;

/**
 * Created by Farades on 24.06.2015.
 */
public class DeviceExceptionFromDb {
    private String description;
    private String time;
    private String state;

    public DeviceExceptionFromDb(String description, String time, String state) {
        this.description = description;
        this.time = time;
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getState() {
//        if (state.equals("start"))
//            return "Начало";
//        return "Конец";
        return state;
    }
}
