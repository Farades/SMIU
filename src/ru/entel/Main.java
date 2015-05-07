package ru.entel;

import ru.entel.devices.Binding;
import ru.entel.devices.MultiMeter;
import ru.entel.protocols.modbus.ModbusFunction;
import ru.entel.protocols.modbus.rtu.master.ModbusMaster;
import ru.entel.protocols.modbus.rtu.master.ModbusSlave;
import ru.entel.protocols.registers.ModbusRegType;
import ru.entel.protocols.service.*;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        ProtocolMasterParams masterParams = new ProtocolMasterParams("COM3", 9600, 8, "none", 1, "rtu", false, 100);
        ProtocolMaster mbMaster = new ModbusMaster("modbus_in", masterParams);

        ProtocolSlaveParams slave1Params = new ProtocolSlaveParams("slave1_1", 1, ModbusFunction.READ_COIL_REGS_1, ModbusRegType.BIT, 0, 6, 50);
        mbMaster.addSlave(new ModbusSlave(slave1Params));

        ProtocolSlaveParams slave2Params = new ProtocolSlaveParams("slave1_2", 1, ModbusFunction.READ_HOLDING_REGS_3, ModbusRegType.INT16, 1, 3, 50);
        mbMaster.addSlave(new ModbusSlave(slave2Params));

        HashMap<String, Binding> elnetBindings = new HashMap<String, Binding>();
        elnetBindings.put("Ua", new Binding("modbus_in", "slave1_2", 1));
        elnetBindings.put("Ub", new Binding("modbus_in", "slave1_2", 2));
        elnetBindings.put("Uc", new Binding("modbus_in", "slave1_2", 3));
        MultiMeter elnet_lte = new MultiMeter(elnetBindings);

        new Thread(mbMaster).start();
    }
}
