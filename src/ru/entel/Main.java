//package ru.entel;
//
//import ru.entel.db.LogSaverDB;
//import ru.entel.devices.Device;
//import ru.entel.engine.Engine;
//
//import javax.naming.NamingException;
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
//
//public class Main {
//    public static void main(String[] args) throws NamingException {
//        MysqlDataSource mysqlDS = new MysqlDataSource();
//        mysqlDS.setURL("jdbc:mysql://localhost:3306/smiu");
//        mysqlDS.setUser("root");
//        mysqlDS.setPassword("qwplzx123");
//        Engine engine = new Engine(mysqlDS);
//        engine.init();
//        engine.run();
//        while(true) {
//            for (Device deivce : engine.getDevices().values()) {
////                System.out.println(deivce.getValues());
//            }
//            LogSaverDB.getDataLogsByCurrentDate();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
