package ru.entel.devices.exceptions;

/**
 * InitParamBindingsException исключение выбрасываемое при инициализации программного устройства с использованием
 * некорреткных параметров. Например: размер равен нулю или битая ссылка.
 */
public class InitParamBindingsException extends Exception {
    public InitParamBindingsException(String s) {
        super(s);
    }
}
