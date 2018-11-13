package nova.android.playvideodemo;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final int KEY_PREPARE = 2020;

    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void initView() {
        mSurfaceView = findViewById(R.id.surfaceView);
    }

    private void initData() {

        try {

            mMediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = getAssets().openFd("test.mp4");
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());


            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(MainActivity.this);

            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener());
            mMediaPlayer.setOnInfoListener(new MediaPlayerInfoListener());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MediaPlayerPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);
        }
    }

    private class MediaPlayerInfoListener implements MediaPlayer.OnInfoListener {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {

            if (what == KEY_PREPARE) {
                beginMPInvoke();
            }

            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mHolder.removeCallback(MainActivity.this);
    }

    private void beginMPInvoke() {
        try {
            Method invoke = MediaPlayer.class.getMethod("invoke", Parcel.class, Parcel.class);
            invoke.setAccessible(true);
            Method newRequest = MediaPlayer.class.getMethod("newRequest");
            newRequest.setAccessible(true);
            Parcel parcel = (Parcel) newRequest.invoke(mMediaPlayer);
            parcel.writeInt(1024);
            Parcel parcelReply = Parcel.obtain();
            invoke.invoke(mMediaPlayer, parcel, parcelReply);
            parcelReply.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
