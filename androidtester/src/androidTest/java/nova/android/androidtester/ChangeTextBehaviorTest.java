package nova.android.androidtester;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * nova.android.androidtester.
 *
 * @author Created by WXG on 2018/5/10 010 14:42.
 * @version V1.0
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChangeTextBehaviorTest {

    private static final String mStringToBeTyped = "Espresso";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.

        onView(withId(R.id.editText)).perform(typeText(mStringToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.btn)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.editText)).check(matches(withText(mStringToBeTyped)));
    }

}
