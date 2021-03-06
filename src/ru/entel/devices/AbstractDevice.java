package ru.entel.devices;

import java.util.HashMap;

/**
 * Created by farades on 07.05.2015.
 */
public abstract class AbstractDevice {
    //Словарь, связывающий определенное значение для мониторинга и с конкретным опрашивающимся устройством и номером его регистра
    protected HashMap<String, Binding> paramsBindings = new HashMap<String, Binding>();
    public AbstractDevice(HashMap<String, Binding> paramsBindings) {
        this.paramsBindings = paramsBindings;
    }
}
