package ru.entel.devices;

/**
 * Created by farades on 07.05.2015.
 */
public class Binding {
    private String masterName;
    private String slaveName;
    private int regNumb;

    public Binding(String masterName, String slaveName, int regNumb) {
        this.slaveName = slaveName;
        this.regNumb = regNumb;
        this.masterName = masterName;
    }

    public String getMasterName() {
        return masterName;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public int getRegNumb() {
        return regNumb;
    }
}
