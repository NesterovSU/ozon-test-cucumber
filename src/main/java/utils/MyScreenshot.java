package utils;

import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestStepFinished;
import io.qameta.allure.Allure;
import io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm;
import managers.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static io.cucumber.plugin.event.Status.FAILED;

/**
 * @author Sergey Nesterov
 */

public class MyScreenshot extends AllureCucumber5Jvm {
    @Override
    public void setEventPublisher(final EventPublisher publisher) {

        publisher.registerHandlerFor(TestStepFinished.class,
                testStepFinished -> {
                    if (testStepFinished.getResult().getStatus().is(FAILED)) {
                        Allure.getLifecycle().addAttachment("screenshot", "image/png", ".png",
                                ((TakesScreenshot) DriverManager.getInstance()).getScreenshotAs(OutputType.BYTES));
                    }
                });

        super.setEventPublisher(publisher);
    }
}