package ru.entel.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.entel.devices.Binding;
import ru.entel.devices.DevType;
import ru.entel.devices.Device;
import ru.entel.devices.DeviceException;
import ru.entel.devices.exceptions.InitParamBindingsException;
import ru.entel.protocols.modbus.ModbusFunction;
import ru.entel.protocols.modbus.rtu.master.ModbusMaster;
import ru.entel.protocols.modbus.rtu.master.ModbusMasterParams;
import ru.entel.protocols.modbus.rtu.master.ModbusSlaveParams;
import ru.entel.protocols.modbus.rtu.master.ModbusSlaveRead;
import ru.entel.protocols.registers.RegType;
import ru.entel.protocols.service.ProtocolMaster;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Farades on 22.05.2015.
 */
public class Configurator {

    public static ProtocolMaster protocolMasterFromJson(String fileName) throws FileNotFoundException {
        exists(fileName);
        StringBuffer sb = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            try {
                String s;
                while((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                br.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Gson gson = new GsonBuilder().registerTypeAdapter(Object.class, new NaturalDeserializer()).create();
        Map jsonParams = (Map) gson.fromJson(sb.toString(), Object.class);

        String protocolType = (String)jsonParams.get("type");

        ProtocolMaster master = null;

        if (protocolType.equals("MODBUS")) {
            Map m_Parameters = (Map)jsonParams.get("m_Parameters");
            String portName = String.valueOf(m_Parameters.get("portName"));
            int baudRate = ((Double)m_Parameters.get("baudRate")).intValue();
            int dataBits = ((Double)m_Parameters.get("databits")).intValue();
            String parity = String.valueOf(m_Parameters.get("parity"));
            int stopBits = ((Double)m_Parameters.get("stopbits")).intValue();
            String encoding = String.valueOf(m_Parameters.get("encoding"));
            boolean echo = (Boolean)m_Parameters.get("echo");
            int timePause = ((Double)jsonParams.get("timePause")).intValue();
            String masterName = String.valueOf(jsonParams.get("name"));

            ModbusMasterParams masterParams = new ModbusMasterParams(portName, baudRate, dataBits, parity, stopBits, encoding, echo, timePause);
            master = new ModbusMaster(masterName, masterParams);

            ArrayList slaves = (ArrayList)jsonParams.get("slaves");
            for (int i = 0; i < slaves.size(); i++) {
                Map slaveParams = (Map)slaves.get(i);
                int unitID = ((Double)slaveParams.get("unitId")).intValue();
                ModbusFunction mbFunc = ModbusFunction.valueOf(String.valueOf(slaveParams.get("mbFunc")));
                RegType regType = RegType.valueOf(String.valueOf(slaveParams.get("mbRegType")));
                int offset = ((Double)slaveParams.get("offset")).intValue();
                int length = ((Double)slaveParams.get("length")).intValue();
                int transDelay = ((Double)slaveParams.get("transDelay")).intValue();
                String slaveName = String.valueOf(slaveParams.get("name"));
                ModbusSlaveParams sp = new ModbusSlaveParams(unitID, mbFunc, regType, offset, length, transDelay);
                master.addSlave(new ModbusSlaveRead(slaveName, sp));
            }
        }
        return master;
    }

    public static Map<String, Device> deviceFromJson(String fileName) {
        Map<String, Device> res = new HashMap<String, Device>();

        HashMap<String, Binding> dirisBindings_1 = new HashMap<String, Binding>();
        dirisBindings_1.put("Ua", new Binding("modbus_in", "slave1_2", 1));
        dirisBindings_1.put("Ub", new Binding("modbus_in", "slave1_2", 2));
        dirisBindings_1.put("Uc", new Binding("modbus_in", "slave1_2", 3));
        dirisBindings_1.put("Uab", new Binding("modbus_in", "slave1_2", 1));
        dirisBindings_1.put("Ubc", new Binding("modbus_in", "slave1_2", 2));
        dirisBindings_1.put("Uca", new Binding("modbus_in", "slave1_2", 3));
        dirisBindings_1.put("Ia", new Binding("modbus_in", "slave1_2", 1));
        dirisBindings_1.put("Ib", new Binding("modbus_in", "slave1_2", 2));
        dirisBindings_1.put("Ic", new Binding("modbus_in", "slave1_2", 3));
        dirisBindings_1.put("F", new Binding("modbus_in", "slave1_2", 3));
        dirisBindings_1.put("P", new Binding("modbus_in", "slave1_2", 3));


        HashMap<String, DeviceException[]> dirisExceptions_1 = new HashMap<String, DeviceException[]>();
        DeviceException ex1 = new DeviceException("Ua", "Ua>750", "Ua больше допустимой нормы");
        DeviceException ex2 = new DeviceException("Ua", "Ua<250", "Ua меньше допустимой нормы");
        DeviceException ex3 = new DeviceException("Ub", "Ub<250", "Ub меньше допустимой нормы");
        DeviceException ex4 = new DeviceException("Ub", "Ub>750", "Ub больше допустимой нормы");
        dirisExceptions_1.put("Ua", new DeviceException[]{ex1, ex2});
        dirisExceptions_1.put("Ub", new DeviceException[]{ex3, ex4});

        HashMap<String, Binding> dirisBindings_2 = new HashMap<String, Binding>();
        dirisBindings_2.put("Ua", new Binding("modbus_in", "slave1_2", 1));
        dirisBindings_2.put("Ub", new Binding("modbus_in", "slave1_2", 2));
        dirisBindings_2.put("Uc", new Binding("modbus_in", "slave1_2", 3));
        dirisBindings_2.put("F", new Binding("modbus_in", "slave1_2", 3));

        HashMap<String, Binding> dirisBindings_3 = new HashMap<String, Binding>();
        dirisBindings_3.put("Ia", new Binding("modbus_in", "slave1_2", 1));
        dirisBindings_3.put("Ib", new Binding("modbus_in", "slave1_2", 2));
        dirisBindings_3.put("Ic", new Binding("modbus_in", "slave1_2", 3));

        try {
            Device d = new Device("Параметры ввода", DevType.MFM, dirisBindings_1, dirisExceptions_1);
            res.put(d.getName(), d);

            d = new Device("Напряжения секции №1", DevType.VOLTMETER, dirisBindings_2, new HashMap<String, DeviceException[]>());
            res.put(d.getName(), d);

            d = new Device("Токи секции №2", DevType.AMPERMETER, dirisBindings_3, new HashMap<String, DeviceException[]>());
            res.put(d.getName(), d);
        } catch (InitParamBindingsException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return res;
    }

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }
}