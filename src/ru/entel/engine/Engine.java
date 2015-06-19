package ru.entel.engine;

import ru.entel.devices.Device;
import ru.entel.protocols.service.ProtocolMaster;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Farades on 22.05.2015.
 */
public class Engine {
    private Map<String, Device> devices = new HashMap();
    private String protocol_json;
    private ProtocolMaster protocolMaster;

    public Engine(String protocol_json) {
        this.protocol_json = protocol_json;
    }

    public void init() {
        devices = Configurator.deviceFromJson("dwa");
        try {
            protocolMaster = Configurator.protocolMasterFromJson(protocol_json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        new Thread(protocolMaster).start();
    }

    public void stop() {
        protocolMaster.stop();
    }

    public Map<String, Device> getDevices() {
        return devices;
    }
}