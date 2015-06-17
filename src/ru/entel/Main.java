//package ru.entel;
//
//import ru.entel.devices.Device;
//import ru.entel.engine.Engine;
//
//import java.util.Map;
//
//public class Main {
//    public static void main(String[] args) {
//        Engine engine = new Engine("config/protocol.json");
//        engine.init();
//        engine.run();
//        while(true) {
//            for (Map.Entry<String, Device> entry : engine.getDevices().entrySet()) {
////                System.out.println(entry.getValue().getValues());
//                System.out.println(entry.getKey());
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
