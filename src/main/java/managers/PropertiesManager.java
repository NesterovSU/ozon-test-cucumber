package managers;

import utils.MyProp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Sergey Nesterov
 */
public class PropertiesManager {

    private static PropertiesManager instance;
    private final Properties PROPERTIES = new Properties();

    public static PropertiesManager getInstance() {
        if (instance == null) instance = new PropertiesManager();
        return instance;
    }

    private PropertiesManager() {
        loadApplicationProperties();
        loadCustomProperties();
        setSystemDriversPaths();
    }

    private void loadApplicationProperties() {
        String fileName = System.getProperty("prop.file", "application");
        try {
            PROPERTIES.load(new FileInputStream("src/main/resources/" + fileName + ".properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomProperties() {
        PROPERTIES.forEach(
                (key, value) -> PROPERTIES.put(key, System.getProperty(key.toString(), value.toString()))
        );
    }

    private void setSystemDriversPaths() {
        System.setProperty("webdriver.gecko.driver", PROPERTIES.get(MyProp.GECKO).toString());
        System.setProperty("webdriver.chrome.driver", PROPERTIES.get(MyProp.CHROME).toString());
        System.setProperty("webdriver.edge.driver", PROPERTIES.get(MyProp.EDGE).toString());
    }

    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
