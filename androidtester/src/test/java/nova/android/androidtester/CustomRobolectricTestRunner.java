package nova.android.androidtester;

import org.junit.runners.model.InitializationError;
import org.robolectric.RoboSettings;
import org.robolectric.RobolectricTestRunner;

/**
 * nova.android.androidtester.
 *
 * @author Created by WXG on 2018/7/19 019 18:08.
 * @version V1.0
 */
public class CustomRobolectricTestRunner extends RobolectricTestRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public CustomRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        RoboSettings.setMavenRepositoryId("alimaven");
        RoboSettings.setMavenRepositoryUrl("http://maven.aliyun.com/nexus/content/groups/public/");
    }
}
