package nova.android.playvideodemo;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * nova.android.playvideodemo.
 *
 * @author Created by WXG on 2018/11/19 019 9:26.
 * @version V1.0
 */
public class CollapsingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String VIDEO_PATH = "test.mp4";
    private static final String TAG = "CollapsingActivity";
    private static final long TIME_STEP = 33;
    private static final int WHAT_UPDATE = 0x01;
    private static final int MAX_FRAMES = 10;

    private static ImageView iv;
    private Button btnStart, btnStop;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
    private long duration;
    private ExtractMpegFramesTest extractMpegFramesTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing);
        initView();
        initData();
    }

    private void initView() {
        iv = findViewById(R.id.iv);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);
    }

    private void initData() {
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            AssetFileDescriptor afd = getAssets().openFd(VIDEO_PATH);
            mediaMetadataRetriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            duration = Long.valueOf(time);

            Log.d(TAG, "duration: " + duration);

        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_start:
                startPlay();
                break;
            case R.id.btn_stop:
                stopPlay();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
    }

    class ShowingRunnable implements Runnable {

        private long currentTime;

        @Override
        public void run() {

            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(currentTime, MediaMetadataRetriever.OPTION_CLOSEST);

            if (bitmap != null) {
                Message message = Message.obtain();
                message.what = WHAT_UPDATE;
                message.obj = bitmap;

                handler.sendMessage(message);
            } else {
                stopPlay();
            }

            currentTime += duration / TIME_STEP * 1000;

            if (currentTime >= duration * 1000) {
                currentTime = 0;
                stopPlay();
            }

            Log.d(TAG, "currentTime: " + currentTime);
        }
    }

    private ShowingRunnable showingRunnable = new ShowingRunnable();

    private UpdateHandler handler = new UpdateHandler(this);

    static class UpdateHandler extends Handler {
        WeakReference<Activity> mWeakReference;

        UpdateHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mWeakReference.get();
            if (activity != null) {
                if (msg.what == WHAT_UPDATE) {
                    Bitmap bitmap = (Bitmap) msg.obj;

                    if (bitmap != null) {
                        iv.setImageBitmap(bitmap);
                    }

                }
            }
        }
    }

    private void stopPlay() {
        executorService.shutdownNow();
//        if (mediaMetadataRetriever != null) {
//            mediaMetadataRetriever.release();
//        }
        if (extractMpegFramesTest != null) {
            extractMpegFramesTest.stop();
        }
    }

    private void startPlay() {
//        executorService.scheduleAtFixedRate(showingRunnable, 0, TIME_STEP, TimeUnit.MILLISECONDS);
        try {
            startCut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCut() throws Exception {

        if (executorService.isShutdown()) {
            executorService = new ScheduledThreadPoolExecutor(1);
        }

        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    extractMpegFramesTest = new ExtractMpegFramesTest();
                    extractMpegFramesTest.setiRender(new ExtractMpegFramesTest.IRender() {
                        @Override
                        public void render(Bitmap bitmap) {
                            if (bitmap != null) {
                                Message message = Message.obtain();
                                message.what = WHAT_UPDATE;
                                message.obj = bitmap;

                                handler.sendMessage(message);
                            }
                        }
                    });
                    extractMpegFramesTest.testExtractMpegFrames();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }, 0, TimeUnit.SECONDS);

    }

}
