package wxgaly.android.downloaddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import nova.android.wxgaly.downloaddemo.R;
import wxgaly.android.downloaddemo.api.FileInfoGetter;
import wxgaly.android.downloaddemo.bean.FileInfo;
import wxgaly.android.downloaddemo.bean.IDownloadCallback;
import wxgaly.android.downloaddemo.util.DownloadUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "wxg";

    public static final String FILENAME = "debug.log";
    public static final String DOWNLOAD_URL = "http://192.168.0.4:8080/download?filename=" + FILENAME;
    public static final String FILEINFO_URL = "http://192.168.0.4:8080/fileInfo?filename=" + FILENAME;
    private static final int THREAD_COUNT = 3;
    private static final int NOTIFY_VALUE = 1024 * 1024 * 2;//1024 * 1024 *
    private List<Long> downloadList = new ArrayList<>(THREAD_COUNT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testDownload();
    }

    private void testDownload() {
        for (int i = 0; i < THREAD_COUNT; i++) {
            downloadList.add(0L);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                new FileInfoGetter().getFileInfo(FILEINFO_URL, new Callback<FileInfo>() {
                    @Override
                    public void onResponse(Call<FileInfo> call, final Response<FileInfo> response) {

                        if (response.isSuccessful()) {
                            final long totalSize = response.body().getFileSize();
                            DownloadUtils downloadUtils = new DownloadUtils();
                            downloadUtils.setDownloadProgress(new IDownloadCallback.IDownloadProgress() {
                                @Override
                                public synchronized void onDownloadProgress(int id, long downloadSize, long blockSize) {

                                    Log.d(TAG, "onDownloadProgress: --id: " + id + "--downloadSize: " + downloadSize
                                            + "-- blockSize : " + blockSize);

                                    downloadList.set(id, downloadSize);

                                    long downloadedSize = 0;
                                    for (Long size : downloadList) {
                                        downloadedSize += size;
                                    }
//
                                    Log.d(TAG, "onDownloadProgress: " + downloadedSize + "---totalSize : " + totalSize);

                                }
                            });
                            downloadUtils.setNotifyProgressValue(NOTIFY_VALUE);
                            downloadUtils.startMultiThread(DOWNLOAD_URL, "/sdcard/test/" + FILENAME, response.body(),
                                    THREAD_COUNT);

                        } else {
                            Log.d(TAG, "onResponse: " + response.isSuccessful());
                        }
                    }

                    @Override
                    public void onFailure(Call<FileInfo> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage(), t);
                    }
                });

            }
        }).start();

    }
}
