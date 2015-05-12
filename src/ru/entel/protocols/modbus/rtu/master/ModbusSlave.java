package ru.entel.protocols.modbus.rtu.master;

import com.adamtaft.eb.EventBusService;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.ModbusIOException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.*;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.procimg.Register;
import ru.entel.events.ModbusDataEvent;
import ru.entel.protocols.modbus.ModbusFunction;
import ru.entel.protocols.modbus.exception.ModbusIllegalRegTypeException;
import ru.entel.protocols.modbus.exception.ModbusNoResponseException;
import ru.entel.protocols.modbus.exception.ModbusRequestException;
import ru.entel.protocols.registers.*;
import ru.entel.protocols.service.ProtocolSlave;
import ru.entel.protocols.service.ProtocolSlaveParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс ModbusSlave - потомок ProtocolSlave.
 * Отвечает за составление запросов, обработку и отправку
 * в EventBus полученной информации от Slave устройств
 * @author Мацепура Артем
 * @version 0.1
 */
public class ModbusSlave extends ProtocolSlave {
    /**
     * Объект для коммункации с COM-портом.
     */
    private SerialConnection con;

    private String masterName;

    /**
     * Адрес slave устройства для обращение по Modbus
     */
    private int unitId;

    /**
     * Номер функции Modbus по которой происходит обращение к Slave устройству
     */
    private ModbusFunction mbFunc;

    /**
     * Тип запрашиваемых регистров (INT16, FLOAT32, BIT)
     */
    private RegType mbRegType;

    /**
     * Номер первого запрашиваемого регистра
     */
    private int offset;

    /**
     * Количество запрашиваемых регистров
     */
    private int length;

    /**
     * Таймаут отклика (время ожидания ответа от физического устройства)
     */
    private int transDelay;

    /**
     * Коллекция в которой хранятся последние значения обработанных регистров
     */
    private Map<Integer, AbstractRegister> registers = new HashMap<Integer, AbstractRegister>();

    public ModbusSlave(String name, ModbusSlaveParams params) {
        super(name, params);
    }

    @Override
    public void init(ProtocolSlaveParams params) {
        if (params instanceof ModbusSlaveParams) {
            ModbusSlaveParams mbParams = (ModbusSlaveParams) params;
            this.unitId = mbParams.getUnitId();
            this.mbFunc = mbParams.getMbFunc();
            this.mbRegType = mbParams.getMbRegType();
            this.offset = mbParams.getOffset();
            this.length = mbParams.getLength();
            this.transDelay = mbParams.getTransDelay();
        } else {
            throw new IllegalArgumentException("Modbus slave params not instance of ModbusSlaveParams");
        }
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    @Override
    public synchronized void request() throws ModbusIllegalRegTypeException, ModbusRequestException, ModbusNoResponseException {
        ModbusRequest req;
        switch (mbFunc) {
            case READ_COIL_REGS_1: {
                req = new ReadCoilsRequest(offset, length);
                break;
            }
            case READ_DISCRETE_INPUT_2: {
                req = new ReadInputDiscretesRequest(offset, length);
                break;
            }
            case READ_HOLDING_REGS_3: {
                req = new ReadMultipleRegistersRequest(offset, length);
                break;
            }
            case READ_INPUT_REGS_4: {
                req = new ReadInputRegistersRequest(offset, length);
                break;
            }
            default:
                throw new IllegalArgumentException("Modbus function incorrect");
        }
        req.setUnitID(this.unitId);
        req.setHeadless();
        ModbusSerialTransaction trans = new ModbusSerialTransaction(this.con);
        trans.setRequest(req);
        trans.setTransDelayMS(this.transDelay);
        switch (mbFunc) {
            case READ_COIL_REGS_1: {
                if (this.mbRegType != RegType.BIT) {
                    throw new ModbusIllegalRegTypeException("Illegal reg type for READ_COIL_REGS_1");
                }
                try {
                    trans.execute();
                    ReadCoilsResponse resp = (ReadCoilsResponse) trans.getResponse();
                    for (int i = 0; i < this.length; i++) {
                        BitRegister reg = new BitRegister(offset + i, resp.getCoils().getBit(i));
                        registers.put(offset + i, reg);
                    }
                } catch (ModbusIOException ex) {
                    //TODO
                    throw new ModbusRequestException("TODO");
                } catch (ModbusException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case READ_DISCRETE_INPUT_2: {
                if (this.mbRegType != RegType.BIT) {
                    throw new ModbusIllegalRegTypeException("Illegal reg type for READ_DISCRETE_INPUT_2");
                }
                try {
                    trans.execute();
                    ReadInputDiscretesResponse resp = (ReadInputDiscretesResponse) trans.getResponse();
                    for (int i = 0; i < this.length; i++) {
                        BitRegister reg = new BitRegister(offset + i, resp.getDiscretes().getBit(i));
                        registers.put(offset + i, reg);
                    }
                } catch (ModbusIOException ex) {
                    //TODO
                    throw new ModbusRequestException("TODO");
                } catch (ModbusException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case READ_HOLDING_REGS_3: {
                try {
                    trans.execute();
                    ModbusResponse tempResp = trans.getResponse();
                    if (tempResp == null) {
                        throw new ModbusNoResponseException("No response to READ HOLDING request.");
                    }
                    if(tempResp instanceof ExceptionResponse) {
                        ExceptionResponse data = (ExceptionResponse)tempResp;
                        System.out.println(data);
                    } else if(tempResp instanceof ReadMultipleRegistersResponse) {
                        ReadMultipleRegistersResponse resp = (ReadMultipleRegistersResponse)tempResp;
                        Register[] values = resp.getRegisters();

                        if (this.mbRegType == RegType.INT16) {
                            for (int i = 0; i < values.length; i++) {
                                Int16Register reg = new Int16Register(this.offset + i, values[i].getValue());
                                registers.put(this.offset + i, reg);
                            }
                        } else if (this.mbRegType == RegType.FLOAT32) {
                            for (int i = 0; i < resp.getWordCount() - 1; i+=2) {
                                Float32Register reg = new Float32Register(offset + i, values[i].getValue(), values[i + 1].getValue());
                                registers.put(this.offset + i, reg);
                            }
                        } else {
                            throw new ModbusIllegalRegTypeException("Illegal reg type for READ_HOLDING_REGS_3");
                        }
                    }
                } catch (ModbusIOException ex) {
                    //TODO
                    throw new ModbusRequestException("TODO");
                } catch (ModbusException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case READ_INPUT_REGS_4: {
                try {
                    trans.execute();
                    ReadInputRegistersResponse resp = (ReadInputRegistersResponse) trans.getResponse();
                    if (this.mbRegType == RegType.INT16) {
                        for (int n = 0; n < resp.getWordCount(); n++) {
                            Int16Register reg = new Int16Register(this.offset + n, resp.getRegisterValue(n));
                            registers.put(offset + n, reg);
                        }
                    } else if (this.mbRegType == RegType.FLOAT32) {
                        for (int i = 0; i < resp.getWordCount()-1; i+=2) {
                            Float32Register reg = new Float32Register(offset + i, resp.getRegisterValue(i), resp.getRegisterValue(i+1));
                            registers.put(this.offset + i, reg);
                        }
                    } else {
                        throw new ModbusIllegalRegTypeException("Illegal reg type for READ_INPUT_REGS_4");
                    }
                } catch (ModbusIOException ex) {
                    //TODO
                    throw new ModbusRequestException("TODO");
                } catch (ModbusException ex) {
                    ex.printStackTrace();
                }
            }
        }
        //TODO перенести общение с шиной в тело, разобраться с исключениями и стабильной работой
        EventBusService.publish(new ModbusDataEvent(this.masterName, this.name, this.registers));
    }

    public void setCon(SerialConnection con) {
        this.con = con;
    }
}
