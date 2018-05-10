package nova.android.androidtester;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * nova.android.androidtester.
 *
 * @author Created by WXG on 2018/5/10 010 10:47.
 * @version V1.0
 */

@RunWith(AndroidJUnit4.class)
public class ActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public ActivityTest(String pkg, Class<MainActivity> activityClass) {
        super(pkg, activityClass);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        mActivity = getActivity();
    }

    @Test
    public void typeOperandsAndPerformAddOperation() {
        // Call the CalculatorActivity add() method and pass in some operand values, then
        // check that the expected value is returned.
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
