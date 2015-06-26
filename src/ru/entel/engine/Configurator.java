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

    public synchronized static ProtocolMaster protocolMasterFromJson(String fileName) throws FileNotFoundException {
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

    public synchronized static Map<String, Device> deviceFromJson(String fileName) throws FileNotFoundException {
        exists(fileName);

        StringBuffer sb = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName) , "UTF-8"));
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

        Map<String, Device> res = new HashMap<String, Device>();
        ArrayList jsonDevices = (ArrayList)jsonParams.get("devices");
        for (Object device : jsonDevices) {
            Map deviceParam = (Map)device;
            String devName = String.valueOf(deviceParam.get("name"));
            String devDescr = String.valueOf(deviceParam.get("description"));
            DevType devtype = DevType.valueOf(String.valueOf(deviceParam.get("devType")));
            ArrayList jsonBindings = (ArrayList)deviceParam.get("bindings");
            HashMap<String, Binding> bindings = new HashMap<String, Binding>();
            for (Object binding : jsonBindings) {
                Map jsonBinding = (Map)binding;
                String varName = String.valueOf(jsonBinding.get("varName"));
                String protocolMasterName = String.valueOf(jsonBinding.get("protocolMasterName"));
                String channelName = String.valueOf(jsonBinding.get("channelName"));
                int regNumb = ((Double)jsonBinding.get("regNumb")).intValue();
                Binding newBinding = new Binding(protocolMasterName, channelName, regNumb);
                bindings.put(varName, newBinding);
            }
            try {
                Device newDevice = new Device(devName, devDescr, devtype, bindings, new HashMap<String, DeviceException[]>());
                res.put(devName, newDevice);
            } catch (InitParamBindingsException e) {
                e.printStackTrace();
                System.exit(1);
            }
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