//package ru.entel;
//
//import ru.entel.devices.Device;
//import ru.entel.engine.Engine;
//
//import javax.activation.DataSource;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import java.util.Hashtable;
//import java.util.Map;
//
//public class Main {
//    public static void main(String[] args) throws NamingException {
//        Hashtable env = new Hashtable();
//        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
//        env.put(Context.PROVIDER_URL, "file:/temp");
//        Context ctx = new InitialContext(env);
//        ClientDataSource dsIn = new org.apache.derby.jdbc.ClientDataSource();
//        Engine engine = new Engine();
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
