package ru.entel.engine;

import ru.entel.db.Database;
import ru.entel.devices.Device;
import ru.entel.devices.LogSaver;
import ru.entel.events.EventBusService;
import ru.entel.protocols.service.ProtocolMaster;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Farades on 22.05.2015.
 */
public class Engine {
    private Map<String, Device> devices = new HashMap();
    private ProtocolMaster protocolMaster;
    private DataSource ds;
    private LogSaver logSaver;

    public Engine(DataSource ds) {
        this.ds = ds;
        Database.setDataSource(ds);
    }

    public void init() {
        //Удаление устройств при переинициализации
        if (devices != null) {
            for (Device device : devices.values()) {
                device.finalize();
                device = null;
            }
            EventBusService.reInit();
            Object obj = new Object();
            WeakReference ref = new WeakReference<Object>(obj);
            obj = null;
            while(ref.get() != null) {
                System.gc();
            }
        }
        try {
            devices = Configurator.deviceFromJson();
            protocolMaster = Configurator.protocolMasterFromJson();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logSaver = new LogSaver(this, 60);
    }

    public void run() {
        new Thread(protocolMaster).start();
        new Thread(logSaver).start();
    }

    public void stop() {
        protocolMaster.stop();
        logSaver.stop();
    }

    public Map<String, Device> getDevices() {
        return devices;
    }
}