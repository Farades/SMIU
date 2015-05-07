package ru.entel.events;

import ru.entel.protocols.registers.AbstractRegister;

import java.util.Map;

/**
 * Created by farades on 07.05.2015.
 */
public class ModbusDataEvent extends Event {
    private String ownerMaster;
    private String ownerSlave;
    private Map<Integer, AbstractRegister> data;

    public ModbusDataEvent(String ownerMaster, String ownerSlave, Map<Integer, AbstractRegister> data) {
        this.ownerMaster = ownerMaster;
        this.ownerSlave = ownerSlave;
        this.data = data;
    }

    public String getOwnerMaster() {
        return ownerMaster;
    }

    public String getOwnerSlave() {
        return ownerSlave;
    }

    public Map<Integer, AbstractRegister> getData() {
        return data;
    }
}
