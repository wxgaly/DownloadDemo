package nova.android.androidtester;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertThat;

/**
 * nova.android.androidtester.
 *
 * @author Created by WXG on 2018/7/19 019 16:37.
 * @version V1.0
 */

@RunWith(CustomRobolectricTestRunner.class)
@Config(manifest=Config.NONE, sdk = 21)
public class MainActivityTest {

    @Test
    public void changeTextTest() {
        AppCompatActivity activity = Robolectric.buildActivity(TwoActivity.class).create().get();

        Button button = activity.findViewById(R.id.btn);

        TextView textView = activity.findViewById(R.id.editText);

        button.performClick();

        assertThat(textView.getText().toString(), CoreMatchers.equalTo("111"));

    }

}
