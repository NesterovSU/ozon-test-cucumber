package utils;

import io.qameta.allure.Allure;
import managers.DriverManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * @author Sergey Nesterov
 */

public class MyScreenshot implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
//        String name = context.getDisplayName();
        Allure.getLifecycle().addAttachment("fail", "image/png", ".png",
                ((TakesScreenshot) DriverManager.getInstance()).getScreenshotAs(OutputType.BYTES));

    }

//    @Override
//    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
//        if (extensionContext.getExecutionException().isPresent()) {
//            String name = extensionContext.getDisplayName();
//            Allure.getLifecycle().addAttachment(name, "image/png", ".png",
//                    ((TakesScreenshot) DriverManager.getInstance()).getScreenshotAs(OutputType.BYTES));
//        }
//    }
}