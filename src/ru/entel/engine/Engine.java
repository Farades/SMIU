package ru.entel.engine;

import ru.entel.db.Database;
import ru.entel.devices.Device;
import ru.entel.protocols.service.ProtocolMaster;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Farades on 22.05.2015.
 */
public class Engine {
    private Map<String, Device> devices = new HashMap();
    private String protocol_json;
    private String devices_json;
    private ProtocolMaster protocolMaster;
    private DataSource ds;

    public Engine(String protocol_json, String devices_json, DataSource ds) {
        this.ds = ds;
        Database.setDataSource(ds);
        this.protocol_json = protocol_json;
        this.devices_json = devices_json;
    }

    public void init() {
        try {
            devices = Configurator.deviceFromJson(devices_json);
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