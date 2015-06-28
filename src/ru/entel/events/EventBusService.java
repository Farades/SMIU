package ru.entel.events;

import net.engio.mbassy.bus.MBassador;

/**
 * Created by Farades on 21.05.2015.
 */
public class EventBusService {
    private static MBassador<ModbusDataEvent> modbusBus = new MBassador<ModbusDataEvent>();

    public static MBassador<ModbusDataEvent> getModbusBus() {
        return modbusBus;
    }

    public static void reInit() {
        modbusBus.shutdown();
        modbusBus = new MBassador<ModbusDataEvent>();
    }
}
