package ru.entel.protocols.modbus;

/**
 * Created by farades on 07.05.2015.
 */
public enum ModbusFunction {
    READ_COIL_REGS_1,
    READ_DISCRETE_INPUT_2,
    READ_HOLDING_REGS_3,
    READ_INPUT_REGS_4,
    WRITE_SINGLE_COIL_5,
    WRITE_SINGLE_REG_6,
    WRITE_MULTIPLE_COILS_15,
    WRITE_MULTIPLE_REGS_16
}
