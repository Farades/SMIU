package ru.entel.devices;

import com.adamtaft.eb.EventBusService;
import com.adamtaft.eb.EventHandler;
import ru.entel.events.ModbusDataEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by farades on 07.05.2015.
 */
public class MultiMeter extends AbstractDevice {
    private float Ua;
    private float Ub;
    private float Uc;
    private int regNumb_Ua;
    private int regNumb_Ub;
    private int regNumb_Uc;

    private Set<String> masterNames = new HashSet<String>();
    private Set<String> slaveNames = new HashSet<String>();

    public MultiMeter(HashMap<String, Binding> paramsBindings) {
        super(paramsBindings);
        this.regNumb_Ua = this.paramsBindings.get("Ua").getRegNumb();
        this.regNumb_Ub = this.paramsBindings.get("Ub").getRegNumb();
        this.regNumb_Uc = this.paramsBindings.get("Uc").getRegNumb();
        for (Binding binding : paramsBindings.values()) {
            masterNames.add(binding.getMasterName());
            slaveNames.add(binding.getSlaveName());
        }
        EventBusService.subscribe(this);
    }

    public boolean isMyEvent(ModbusDataEvent evt) {
        String ownerMaster = evt.getOwnerMaster();
        String ownerSlave = evt.getOwnerSlave();
        if (this.masterNames.contains(ownerMaster) && this.slaveNames.contains(ownerSlave)) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void handleModbusDataEvent(ModbusDataEvent evt) {
        if (isMyEvent(evt)) {
            this.Ua = (Integer) evt.getData().get(regNumb_Ua).getValue();
            this.Ub = (Integer) evt.getData().get(regNumb_Ub).getValue();
            this.Uc = (Integer) evt.getData().get(regNumb_Uc).getValue();
            System.out.println(this);
        }
    }

    @Override
    public String toString() {
        return "MultiMeter{" +
                "Ua=" + Ua +
                ", Ub=" + Ub +
                ", Uc=" + Uc +
                '}';
    }
}
