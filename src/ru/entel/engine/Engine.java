package ru.entel.engine;

import ru.entel.devices.Device;

import java.io.FileNotFoundException;

/**
 * Created by Farades on 22.05.2015.
 */
public class Engine {
    public void run() {
        Device dev = Configurator.deviceFromJson("dwa");
        try {
            new Thread(Configurator.protocolMasterFromJson("config/protocol.json")).run();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
