package ru.entel.rpi;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Created by Farades on 29.07.2015.
 */
public class GpioInput {
//    private GpioPinDigitalInput pin;
//
//    public GpioInput(Pin pinNumber, PinPullResistance pullResistance) {
//        GpioController gpio = GpioFactory.getInstance();
//        pin = gpio.provisionDigitalInputPin(pinNumber, pullResistance);
//
//        pin.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//                System.out.println("--> GPIO PIN STATE CHANGE: " + pin + " = " + pin.getState());
//            }
//        });
//    }
}
