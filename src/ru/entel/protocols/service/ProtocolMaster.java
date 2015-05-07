package ru.entel.protocols.service;

/**
 * Created by farades on 07.05.2015.
 */
public abstract class ProtocolMaster implements Runnable {
    protected String name;
    //Конструктор, запрещающий использовать конструктор по умолчанию для потомков
    public ProtocolMaster(String name, ProtocolMasterParams params) {
        this.name = name;
    }

    public abstract void addSlave(ProtocolSlave slave);
}
