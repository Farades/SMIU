package ru.entel.protocols.service;

import java.util.HashSet;

/**
 * Created by farades on 07.05.2015.
 */
public interface ProtocolInterface extends Runnable {
    public void addSlave(ProtocolSlave slave);
}
