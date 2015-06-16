//package ru.entel;
//
//import ru.entel.engine.Engine;
//
//public class Main {
//    public static void main(String[] args) {
//        Engine engine = new Engine("config/protocol.json");
//        engine.run();
//        while(true) {
//            System.out.println(engine.getDevices().get("Diris A").getValues());
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
