package wxgaly.android.downloaddemo;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nova.android.wxgaly.downloaddemo.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wxgaly.android.downloaddemo.api.FileInfoGetter;
import wxgaly.android.downloaddemo.bean.FileInfo;
import wxgaly.android.downloaddemo.bean.IDownloadCallback;
import wxgaly.android.downloaddemo.util.DownloadUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "wxg";

    public static final String FILENAME = "debug.log";
    public static final String DOWNLOAD_URL = "http://192.168.0.4:8080/download?filename=" + FILENAME;
    public static final String FILEINFO_URL = "http://192.168.0.4:8080/fileInfo?filename=" + FILENAME;
    private static final int THREAD_COUNT = 3;
    private static final int NOTIFY_VALUE = 1024 * 1024 * 2;//1024 * 1024 *
    private List<Long> downloadList = new ArrayList<>(THREAD_COUNT);

    public static final String CUSTOM_FONT_XINWEI = "/sdcard/nova/viplex_terminal/font/STXINWEI.TTF";
    public static final String CUSTOM_FONT_XIHEI = "/sdcard/nova/viplex_terminal/font/STXIHEI.TTF";
    public static final String CUSTOM_FONT_SIMLI = "/sdcard/nova/viplex_terminal/font/SIMLI.TTF";
    public static final String CUSTOM_FONT_STSONG = "/sdcard/nova/viplex_terminal/font/STSONG.TTF";
    public static final String CUSTOM_FONT_STKAITI = "/sdcard/nova/viplex_terminal/font/STKAITI.TTF";
    public static final String CUSTOM_FONT_STLITI = "/sdcard/nova/viplex_terminal/font/STLITI.TTF";
    public static final String CUSTOM_FONT_WXG = "/sdcard/nova/viplex_terminal/font/wxgyh.TTF";

    public static final String CUSTOM_FONT_MICROSOFT_YAHEI = "Microsoft YaHei";
    public static final String CUSTOM_FONT_SONGTI = "SimSun";
    public static final String CUSTOM_FONT_KAITI = "KaiTi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        testDownload();
//        testFontHeight();
//        testShell();

    }

    private void testShell() {
        enterShell();
    }

    public static boolean enterShell() {

        boolean result = false;

        try {

            Process p = Runtime.getRuntime().exec("su");
//            Process p = Runtime.getRuntime().exec("sh");


            InputStream input = p.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(input));

            StringBuffer stringBuffer = new StringBuffer();

            String content = "";

            // 读取ping的内容，可不加。
            DataOutputStream  os = new DataOutputStream(p.getOutputStream());

            os.write("echo AT+CRESET > /dev/ttyUSB2".getBytes());
            os.writeBytes("\n");
            os.flush();

//            os.write("mkdir a".getBytes());
//            os.writeBytes("\n");
//            os.flush();


            // PING的状态

//            int status = p.waitFor();
//
//            if (status == 0) {
//
//                result = true;
//
//            }

            Log.d(TAG, "enterShell: " + stringBuffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public String execute(String[] command, String directory) throws IOException {
        String result = "";
        String[] suC = {"su"};
        try {

            ProcessBuilder builder = new ProcessBuilder(suC);
            if (directory != null)
                builder.directory(new File(directory));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            DataOutputStream os;
            os = new DataOutputStream(process.getOutputStream());
            InputStream is = process.getInputStream();

            os.writeBytes("ls\n");
            os.flush();
            os.close();
            byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                result = result + new String(buffer);
            }
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void testFontHeight() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int textSize = 10;

                while (textSize <= 100) {
                    initPaint(textSize);
                    textSize += 10;
                }
            }
        }).start();
    }

    private void initPaint(int textSize) {
        Paint paint = new Paint();
        try {
            Typeface typeface = Typeface.createFromFile(CUSTOM_FONT_WXG);
//            Typeface typeface = Typeface.create(CUSTOM_FONT_KAITI, Typeface.NORMAL);

            paint.setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paint.setColor(Color.RED);
        paint.setTextSize(textSize);
        Rect rect = new Rect();
        paint.getTextBounds("真", 0, 1, rect);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
//        Log.d(TAG, "textSize : " + textSize + "----height: " + (fontMetrics.bottom - fontMetrics.top));
//        Log.d(TAG, " rect ----height: " + rect.height());
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
