package ru.entel.protocols.service;

import ru.entel.protocols.modbus.exception.ModbusIllegalRegTypeException;
import ru.entel.protocols.modbus.exception.ModbusNoResponseException;
import ru.entel.protocols.modbus.exception.ModbusRequestException;

/**
 * Created by farades on 07.05.2015.
 */
public abstract class ProtocolSlave {
    public ProtocolSlave(ProtocolSlaveParams params) {
    }

    public abstract void request() throws Exception;
}
