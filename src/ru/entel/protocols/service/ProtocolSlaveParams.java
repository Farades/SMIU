package ru.entel.protocols.service;

import ru.entel.protocols.modbus.ModbusFunction;
import ru.entel.protocols.registers.ModbusRegType;

/**
 * Created by farades on 07.05.2015.
 */
public class ProtocolSlaveParams {
    private String name;
    private int unitId;
    private ModbusFunction mbFunc;
    private ModbusRegType mbRegType;
    private int offset;
    private int length;
    private int transDelay;

    public ProtocolSlaveParams(String name, int unitId, ModbusFunction mbFunc, ModbusRegType mbRegType, int offset, int length, int transDelay) {
        this.name = name;
        this.unitId = unitId;
        this.mbFunc = mbFunc;
        this.mbRegType = mbRegType;
        this.offset = offset;
        this.length = length;
        this.transDelay = transDelay;
    }

    public String getName() {
        return name;
    }

    public int getUnitId() {
        return unitId;
    }

    public ModbusFunction getMbFunc() {
        return mbFunc;
    }

    public ModbusRegType getMbRegType() {
        return mbRegType;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public int getTransDelay() {
        return transDelay;
    }
}
