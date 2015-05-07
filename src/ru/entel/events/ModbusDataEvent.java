package ru.entel.events;

import ru.entel.protocols.modbus.registers.ModbusAbstractRegister;

import java.util.Map;

/**
 * Created by farades on 07.05.2015.
 */
public class ModbusDataEvent extends Event {
    private String owner;
    private Map<Integer, ModbusAbstractRegister> data;

    public ModbusDataEvent(String owner, Map<Integer, ModbusAbstractRegister> data) {
        this.owner = owner;
        this.data = data;
    }

    public String getOwner() {
        return owner;
    }

    public Map<Integer, ModbusAbstractRegister> getData() {
        return data;
    }
}
