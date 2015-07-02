//package ru.entel;
//
//import ru.entel.devices.Device;
//import ru.entel.engine.Engine;
//
//import javax.naming.NamingException;
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
//import java.util.Map;
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
//            for (Map.Entry<String, Device> entry : engine.getDevices().entrySet()) {
//                System.out.println(entry.getValue().getValues());
////                System.out.println(entry.getKey());
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
