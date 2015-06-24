package ru.entel.db;

/**
 * Created by Farades on 24.06.2015.
 */
public class DeviceExceptionFromDb {
    private String deviceOnwer;
    private String description;
    private String time_start;
    private String time_end;

    public DeviceExceptionFromDb(String deviceOnwer, String description, String time_start, String time_end) {
        this.deviceOnwer = deviceOnwer;
        this.description = description;
        this.time_start = time_start;
        this.time_end = time_end;
    }

    public String getDeviceOnwer() {
        return deviceOnwer;
    }

    public String getDescription() {
        return description;
    }

    public String getTime_start() {
        return time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    @Override
    public String toString() {
        return "DeviceExceptionFromDb{" +
                "deviceOnwer='" + deviceOnwer + '\'' +
                ", description='" + description + '\'' +
                ", time_start='" + time_start + '\'' +
                ", time_end='" + time_end + '\'' +
                '}';
    }
}
