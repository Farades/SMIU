package ru.entel.devices;

import ru.entel.protocols.registers.AbstractRegister;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Farades on 23.06.2015.
 */
public class DeviceException {
    private String varOwnerName;
    private String condition;
    private String description;
    private AbstractRegister currentValue;
    private boolean active;

    public DeviceException(String varOwnerName, String condition, String description) {
        this.varOwnerName = varOwnerName;
        this.condition = condition;
        this.description = description;
        active = false;
    }

    public boolean check(AbstractRegister value) {
        this.currentValue = value;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        String script = condition.replace(varOwnerName, value.toString());
        try {
            Boolean res = (Boolean)engine.eval(script);
            if (res) {
                this.activate();
            } else {
                this.desactivate();
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return this.isActive();
    }

    public boolean isActive() {
        return active;
    }

    public String getDescription() {
        return description;
    }

    public String getVarOwnerName() {
        return varOwnerName;
    }

    public AbstractRegister getCurrentValue() {
        return currentValue;
    }

    public void activate() {
        this.active = true;
    }

    public void desactivate() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "DeviceException{" +
                "condition='" + condition + '\'' +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceException that = (DeviceException) o;

        return condition.equals(that.condition);

    }

    @Override
    public int hashCode() {
        return condition.hashCode();
    }
}
