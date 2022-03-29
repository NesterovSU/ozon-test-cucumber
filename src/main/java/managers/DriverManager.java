package managers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.MyProp;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * @author Sergey Nesterov
 */
public class DriverManager {
    private static WebDriver driver;
    private static final PropertiesManager properties = PropertiesManager.getInstance();

    public static WebDriver getInstance() {
        return driver != null ? driver : create();
    }

    private static WebDriver create() {
        String browser = properties.get(MyProp.BROWSER);
        switch (browser == null ? "chrome" : browser) {
            case "chrome":
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications");
                driver = new ChromeDriver(options);
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "remote.chrome":
                createRemoteDriver("chrome", "84.0");
                break;
            case "remote.firefox":
                createRemoteDriver("firefox", "78.0");
                break;
            default:
                driver = new ChromeDriver();
        }

        long implWait = Long.parseLong(properties.get(MyProp.IMPLWAIT));
        long pgLdTt = Long.parseLong(properties.get(MyProp.PGLDWAIT));
        driver.manage().timeouts().implicitlyWait(implWait, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(pgLdTt, TimeUnit.SECONDS);
//        webDriver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().window().maximize();
        return driver;
    }

    private static void createRemoteDriver(String browser, String version) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);
        capabilities.setVersion(version);
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", false);

        try {
            RemoteWebDriver driver = new RemoteWebDriver(
                    URI.create("http://51.250.100.60:4444/wd/hub").toURL(),
                    capabilities
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
