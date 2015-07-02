package ru.entel.devices;

import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;
import org.apache.log4j.Logger;
import ru.entel.devices.exceptions.IncorrectDeviceBindingException;
import ru.entel.devices.exceptions.InitParamBindingsException;
import ru.entel.events.EventBusService;
import ru.entel.events.ModbusDataEvent;
import ru.entel.protocols.registers.AbstractRegister;
import ru.entel.protocols.registers.ZeroRegister;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Мацепура Артем
 * @version 0.1
 */
//@Listener(references= References.Strong)
public class Device extends AbstractDevice implements Serializable {
    private static final Logger logger = Logger.getLogger(AbstractDevice.class);

    /**
     * Словарь, содержащий исключительные ситуации
     */
    HashMap<String, ArrayList<DeviceException>> exceptions = new HashMap<String, ArrayList<DeviceException>>();

    /**
     * Сет, содержащий активные исключения для данного устройства
     */
    Set<DeviceException> activeExceptions = new HashSet<DeviceException>();

    /**
     * Название устройства
     */
    private String name;

    /**
     * Описание устройства
     */
    private String description;

    /**
     * Тип устройства
     */
    private DevType type;

    /**
     * Коллекция, хранящая актуальные значения параметров по всем каналам для данного устройства
     */
    private Map<String, AbstractRegister> values = new HashMap<String, AbstractRegister>();

    /**
     * Коллекция, хранящая биндинги для каждого конкретного канала
     */
    private Map<String, Map<String, Binding>> channelsBindings = new HashMap<String, Map<String, Binding>>();

    /**
     * Коллекция, хранящая все каналы с которыми работает данное устройство
     */
    private Set<String> channelsId = new HashSet<String>();

    /**
     * Конструктор
     * @param paramsBindings Ассоциативный массив в котором ключом является название параметра,
     *                       а значением является объект класса Binding.
     * @throws InitParamBindingsException Исключение, вызываемое передачей некорректных биндингов.
     */
    public Device(String name, String description, DevType type, HashMap<String, Binding> paramsBindings, HashMap<String, ArrayList<DeviceException>> exceptions) throws InitParamBindingsException {
        super(paramsBindings);
        this.name = name;
        this.description = description;
        this.type = type;
        this.exceptions = exceptions;
        if ((paramsBindings == null) || (paramsBindings.size() == 0)) {
            throw new InitParamBindingsException("Params bindings incorrect (==null or size == 0)");
        }
        //Разбиение биндингов по каналам. На выходе получается коллекция channelsBindings
        for (Map.Entry<String, Binding> entry : paramsBindings.entrySet()) {
            this.values.put(entry.getKey(), new ZeroRegister());
            if (!channelsBindings.containsKey(entry.getValue().getChannelID())) {
                Map<String, Binding> tempMap = new HashMap<String, Binding>();
                tempMap.put(entry.getKey(), entry.getValue());
                channelsBindings.put(entry.getValue().getChannelID(), tempMap);
            } else {
                Map<String, Binding> tempMap = channelsBindings.get(entry.getValue().getChannelID());
                tempMap.put(entry.getKey(), entry.getValue());
                channelsBindings.put(entry.getValue().getChannelID(), tempMap);
            }
        }
        //Инициализация сета channelsId
        for (Binding binding : paramsBindings.values()) {
            channelsId.add(binding.getChannelID());
        }
        EventBusService.getModbusBus().subscribe(this);
    }

    public void finalize() {
        EventBusService.getModbusBus().unsubscribe(this);
    }

    /**
     * Метод isMyEvent проверяет предназначлся ли переданный Event данному устройству.
     * @param evt Event, который необходимо проверить.
     * @return true если предназначался, в противном случае false
     */
    public boolean isMyEvent(ModbusDataEvent evt) {
        return this.channelsId.contains(evt.getOwnerID());
    }

    /**
     * Метод handleModbusDataEvent обрабатывает ModbusDataEvent в программной шине EventBusService
     * @param evt Event, который необходимо обработать
     */
    @Handler
    public void handleModbusDataEvent(ModbusDataEvent evt) throws IncorrectDeviceBindingException{

        if (isMyEvent(evt)) {
            for (Map.Entry<String, Binding> cbEntrySet : channelsBindings.get(evt.getOwnerID()).entrySet()) {
                AbstractRegister value = null;
                for (Map.Entry<Integer, AbstractRegister> valuesEntrySet : evt.getData().entrySet()) {
                    if (valuesEntrySet.getKey() == cbEntrySet.getValue().getRegNumb()) {
                        value = valuesEntrySet.getValue();
                        break;
                    }
                }
                if (value != null) {
                    this.values.put(cbEntrySet.getKey(), value);
                    //Обработка исключительных ситуаций
                    //Если для обновленной переменной объекта Device существуют исключительные ситуации, то проверяем
                    //Не произошла ли какая-либо исключительная ситуация
//                    if (this.exceptions.containsKey(cbEntrySet.getKey())) {
//                        for (DeviceException deviceException : exceptions.get(cbEntrySet.getKey())) {
//                            if (deviceException.check(value)) {
//                                this.activeExceptions.add(deviceException);
//                            } else {
//                                this.activeExceptions.remove(deviceException);
//                            }
//                        }
//                    }
                } else {
                    throw new IncorrectDeviceBindingException("No register for binding: " + cbEntrySet.getValue());
                }
            }
            logger.debug("\"" + this.name +"\" update values: " + this.values);
        }
    }
    @Override
    public String toString() {
        return "[" + this.name + "] "
                + this.values;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, AbstractRegister> getValues() {
        return values;
    }

    public DevType getType() {
        return type;
    }

    public Set<DeviceException> getActiveExceptions() {
        return activeExceptions;
    }
}
