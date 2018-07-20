package nova.android.androidtester.test;

/**
 * nova.android.androidtester.test.
 *
 * @author Created by WXG on 2018/7/20 020 8:47.
 * @version V1.0
 */
public interface FinishListener {

    void onActivityFinished();
    void dumpIntermediateCoverage(String filePath);

}
