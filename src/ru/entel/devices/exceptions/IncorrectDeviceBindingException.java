package ru.entel.devices.exceptions;

/**
 * IncorrectDeviceBindingException исключение, выбрасываемое при некорректном биндинге.
 * Например: регистр, описанный в биндинге не опрашивается в данном Slave устройстве.
 */
public class IncorrectDeviceBindingException extends Exception {
    public IncorrectDeviceBindingException(String s) {
        super(s);
    }
}
