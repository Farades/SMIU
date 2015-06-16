package ru.entel.engine;

import ru.entel.devices.Device;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Farades on 22.05.2015.
 */
public class Engine {
    private Map<String, Device> devices = new HashMap();
    private String protocol_json;

    public Engine(String protocol_json) {
        this.protocol_json = protocol_json;
    }

    public void run() {
        Device dev = Configurator.deviceFromJson("dwa");
        devices.put(dev.getName(), dev);
        try {
            new Thread(Configurator.protocolMasterFromJson(protocol_json)).start();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, Device> getDevices() {
        return devices;
    }
}