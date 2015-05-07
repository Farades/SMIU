package ru.entel.protocols.registers;

/**
 * Created by farades on 07.05.2015.
 */
public class BitRegister extends AbstractRegister {
    private ModbusRegType regType;

    public BitRegister(int regNumb, boolean value) {
        this.regType = ModbusRegType.BIT;
        this.regNumb = regNumb;
        this.value = value ? 1 : 0;
    }

    public ModbusRegType getRegType() {
        return regType;
    }
}
