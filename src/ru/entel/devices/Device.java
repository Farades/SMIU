package ru.entel.devices;

import com.adamtaft.eb.EventBusService;
import com.adamtaft.eb.EventHandler;
import ru.entel.devices.exceptions.IncorrectDeviceBindingException;
import ru.entel.devices.exceptions.InitParamBindingsException;
import ru.entel.events.ModbusDataEvent;
import ru.entel.protocols.registers.AbstractRegister;
import ru.entel.protocols.registers.ZeroRegister;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Мацепура Артем
 * @version 0.1
 */
public class Device extends AbstractDevice {
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
    public Device(HashMap<String, Binding> paramsBindings) throws InitParamBindingsException {
        super(paramsBindings);
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
        EventBusService.subscribe(this);
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
    @EventHandler
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
                } else {
                    throw new IncorrectDeviceBindingException("No register for binding: " + cbEntrySet.getValue());
                }
            }
            System.out.println(this.values);
        }
    }
}
