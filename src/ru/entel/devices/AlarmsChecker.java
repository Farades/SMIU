package ru.entel.devices;

import ru.entel.engine.Engine;

import java.util.*;

/**
 *  ласс, провер€ющий в отдельном потоке все аварии дл€ всех устройств
 */
public class AlarmsChecker implements Runnable {
    private Engine engine;
    private int timePause;
    private Map<String, Set<DeviceException>> activeException = new HashMap<String, Set<DeviceException>>();
    private volatile boolean running = true;


    public AlarmsChecker(Engine engine, int timePause) {
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
            synchronized (this.activeException) {
                this.activeException.clear();
                for (Device device : engine.getDevices().values()) {
                    Set<DeviceException> deviceActiveEx = new HashSet<DeviceException>();
                    for (DeviceException deviceException : device.getAlarms()) {
                        String varName = deviceException.getVarOwnerName();
                        if (deviceException.check(device.getValues().get(varName))) {
                            deviceActiveEx.add(deviceException);
                        }
                    }
                    if (deviceActiveEx.size() > 0) {
                        this.activeException.put(device.getDescription(), deviceActiveEx);
                    }
                }
            }
        }
    }

    public Map<String, Set<DeviceException>> getActiveException() {
        return activeException;
    }

    public synchronized void stop() {
        this.running = false;
    }
}
