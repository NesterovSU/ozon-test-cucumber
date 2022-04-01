import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * @author Sergey Nesterov
 */

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"steps"},
        tags = {"@myTag"},
        plugin = {"utils.MyScreenshot"}
)
public class CucumberRunner {
}
