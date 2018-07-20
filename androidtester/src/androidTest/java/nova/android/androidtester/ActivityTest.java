package nova.android.androidtester;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * nova.android.androidtester.
 *
 * @author Created by WXG on 2018/5/10 010 10:47.
 * @version V1.0
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ActivityTest {


    public ActivityTest() {
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
//        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        mActivity = getActivity();
    }

    @Test
    public void typeOperandsAndPerformAddOperation() {
        // Call the CalculatorActivity add() method and pass in some operand values, then
        // check that the expected value is returned.
    }

    @After
    public void tearDown() throws Exception {
//        super.tearDown();
    }

}
