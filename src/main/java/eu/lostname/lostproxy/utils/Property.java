package eu.lostname.lostproxy.utils;

import java.io.*;
import java.util.Properties;

public class Property {

    /**
     * @param file name of the file
     * @param key  the key to look for in the config
     * @return value of the given key
     */
    public String get(String file, String key) {
        try (InputStream input = new FileInputStream("LostProxy/" + file + ".properties")) {

            Properties prop = new Properties();

            // load a properties file from InputStream
            prop.load(input);

            return prop.getProperty(key);

            // Java 8 , print key and values
//            prop.forEach((key, value) -> System.out.println("Key : " + key + ", Value : " + value));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * create the default properties-file
     */
    public void setDefaultProps() {
        //create the file if not exists
        File dir = new File("LostProxy");
        if (!dir.exists()) {
            dir.mkdirs();
            try (OutputStream output = new FileOutputStream("LostProxy/cfg.properties")) {

                Properties prop = new Properties();

                // set the properties value
                prop.setProperty("db.port", "27017");
                prop.setProperty("db.host", "localhost");
                prop.setProperty("db.username", "root");
                prop.setProperty("db.password", "root");
                prop.setProperty("db.database", "root");

                prop.setProperty("ts.username", "serveradmin");
                prop.setProperty("ts.password", "password");
                prop.setProperty("ts.hostname", "localhost");
                prop.setProperty("ts.queryPort", "10011");
                prop.setProperty("ts.virtualServerPort", "9987");
                prop.setProperty("ts.nickname", "LostProxy - TeamSpeakManager");

                // save properties to project folder
                prop.store(output, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}