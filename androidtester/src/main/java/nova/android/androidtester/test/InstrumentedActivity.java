package nova.android.androidtester.test;

import nova.android.androidtester.MainActivity;

/**
 * nova.android.androidtester.test.
 *
 * @author Created by WXG on 2018/7/20 020 8:48.
 * @version V1.0
 */
public class InstrumentedActivity extends MainActivity {

    public FinishListener finishListener ;
    public void  setFinishListener(FinishListener finishListener){
        this.finishListener = finishListener;
    }

    @Override
    public void onDestroy() {
        if (this.finishListener !=null){
            finishListener.onActivityFinished();
        }
        super.onDestroy();
    }

}
