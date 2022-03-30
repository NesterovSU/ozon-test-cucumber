package utils;

import io.qameta.allure.Allure;
import managers.DriverManager;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * @author Sergey Nesterov
 */

public class MyScreenshot implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext extensionContext){
        if (extensionContext.getExecutionException().isPresent()) {
            Allure.getLifecycle().addAttachment("screenshot", "image/png", ".png",
                    ((TakesScreenshot) DriverManager.getInstance()).getScreenshotAs(OutputType.BYTES));
        }
    }
}