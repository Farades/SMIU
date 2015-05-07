package ru.entel.protocols.service;

/**
 * Created by farades on 07.05.2015.
 */
public abstract class ProtocolMaster implements ProtocolInterface{
    //Конструктор, запрещающий использовать конструктор по умолчанию для потомков
    public ProtocolMaster(ProtocolMasterParams params) {
    }
}
