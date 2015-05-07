package ru.entel.protocols.registers;

/**
 * Created by farades on 07.05.2015.
 */
public class Int16Register extends AbstractRegister {
    private ModbusRegType regType;

    public Int16Register(int regNumb, int value) {
        this.regNumb = regNumb;
        this.regType = ModbusRegType.INT16;
        this.value = value;
    }

    public ModbusRegType getRegType() {
        return regType;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
