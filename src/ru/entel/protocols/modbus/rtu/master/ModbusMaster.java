package ru.entel.protocols.modbus.rtu.master;

import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import ru.entel.protocols.service.ProtocolMaster;
import ru.entel.protocols.service.ProtocolMasterParams;
import ru.entel.protocols.service.ProtocolSlave;

import java.util.HashSet;

/**
 * Класс ModbusMaster - потомок ProtocolMaster.
 * Реализует интерфейс Runnable.
 * Отвечает за добавление, хранение и управление
 * объектами класса ModbusSlave в отдельном потоке.
 * @author Мацепура Артем
 * @version 0.1
 */
public class ModbusMaster extends ProtocolMaster {
    /**
     * Объект для коммункации с COM-портом. Передается каждому ModbusSlave при добавлении
     */
    private SerialConnection con;

    /**
     * Время задержки между вызовами метода request у объектов ModbusSlave
     */
    private int timePause;

    /**
     * Коллекция в которой хранятся все объекты ModbusSlave для данного мастера
     */
    private HashSet<ModbusSlave> slaves = new HashSet<ModbusSlave>();

    /**
     * Флаг для остановки отдельного потока опроса объектов ModbusSlave
     */
    public volatile boolean running = true;

    /**
     * Конструктор
     * @param name название данного ModbusMaster (Например: modbus_in, modbus_1)
     * @param params объект, принадлежащий классу, унаследованному от ProtocolMasterParams.
     *               Хранит в себе необходимые параметры для инициализации ModbusMaster.
     */
    public ModbusMaster(String name, ModbusMasterParams params) {
        super(name, params);
    }

    @Override
    public void init(ProtocolMasterParams params) {
        ModbusCoupler.getReference().setUnitID(128);
        SerialParameters SerialParams = new SerialParameters();
        if (params instanceof ModbusMasterParams) {
            ModbusMasterParams mbParams = (ModbusMasterParams) params;
            SerialParams.setPortName(mbParams.getPortName());
            SerialParams.setBaudRate(mbParams.getBaudRate());
            SerialParams.setDatabits(mbParams.getDataBits());
            SerialParams.setParity(mbParams.getParity());
            SerialParams.setStopbits(mbParams.getStopbits());
            SerialParams.setEncoding(mbParams.getEncoding());
            SerialParams.setEcho(mbParams.getEcho());
            this.timePause = mbParams.getTimePause();
            con = new SerialConnection(SerialParams);
        } else {
            //TODO добавить исключение
        }
    }

    /**
     * Метод, добавляющий ModbusSlave в коллекцию данного ModbusMaster для последующего опроса.
     * @param slave Объект типа ModbusSlave, суженный до родительского типа ProtocolSlave.
     */
    @Override
    public void addSlave(ProtocolSlave slave) {
        ModbusSlave mbSlave = (ModbusSlave) slave;
        mbSlave.setMasterName(this.name);
        mbSlave.setCon(this.con);
        slaves.add(mbSlave);
    }

    /**
     * Метод, открывающий соединение с COM-портом
     */
    public void openPort() {
        try {
            this.con.open();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод, закрывающий соединение с COM-портом
     */
    public void closePort() {
        this.con.close();
    }

    /**
     * Реализация интерфейса Runnable. Необходима для бесконечного цикла опроса ModbusSlave в отдельном потоке.
     */
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
