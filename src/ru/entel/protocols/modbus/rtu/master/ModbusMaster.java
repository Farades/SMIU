package ru.entel.protocols.modbus.rtu.master;

import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import ru.entel.protocols.service.ProtocolMaster;
import ru.entel.protocols.service.ProtocolMasterParams;
import ru.entel.protocols.service.ProtocolSlave;

import java.util.HashSet;

/**
 * Created by farades on 07.05.2015.
 */
public class ModbusMaster extends ProtocolMaster {
    private SerialConnection con;
    private int timePause;
    private HashSet<ModbusSlave> slaves = new HashSet<ModbusSlave>();
    public volatile boolean running = true;

    public ModbusMaster(String name, ProtocolMasterParams params) {
        super(name, params);
        this.timePause = params.getTimePause();
        ModbusCoupler.getReference().setUnitID(128);
        SerialParameters SerialParams = new SerialParameters();
        SerialParams.setPortName(params.getPortName());
        SerialParams.setBaudRate(params.getBaudRate());
        SerialParams.setDatabits(params.getDataBits());
        SerialParams.setParity(params.getParity());
        SerialParams.setStopbits(params.getStopbits());
        SerialParams.setEncoding(params.getEncoding());
        SerialParams.setEcho(params.getEcho());
        con = new SerialConnection(SerialParams);
    }

    @Override
    public void addSlave(ProtocolSlave slave) {
        ModbusSlave mbSlave = (ModbusSlave) slave;
        mbSlave.setMasterName(this.name);
        mbSlave.setCon(this.con);
        slaves.add(mbSlave);
    }

    public void openPort() {
        try {
            this.con.open();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void closePort() {
        this.con.close();
    }

    @Override
    public void run() {
        if (slaves.size() != 0) {
            openPort();
            while(running) {
                for (ModbusSlave slave : slaves) {
                    try {
                        slave.request();
                        Thread.sleep(timePause);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        //TODO
                        ex.printStackTrace();
                    }
                }
            }
            closePort();
        }
    }
}
