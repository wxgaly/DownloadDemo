package nova.android.androidtester;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * nova.android.androidtester.
 *
 * @author Created by WXG on 2018/7/19 019 18:17.
 * @version V1.0
 */
public class TwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
