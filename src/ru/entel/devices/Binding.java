package ru.entel.devices;

/**
 * Created by farades on 07.05.2015.
 */
public class Binding {
    private String slaveName;
    private int regNumb;

    public Binding(String slaveName, int regNumb) {
        this.slaveName = slaveName;
        this.regNumb = regNumb;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public int getRegNumb() {
        return regNumb;
    }
}
