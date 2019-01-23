package nova.android.playvideodemo;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final int KEY_PREPARE = 2020;
    private static final String PATH = "test.mp4";

    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mHolder;
    private EditText mEditTextVideoPath;
    private Button mBtnStartPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void initView() {
        mSurfaceView = findViewById(R.id.surfaceView);
        mEditTextVideoPath = findViewById(R.id.et_path);
        mBtnStartPlay = findViewById(R.id.btn_start);

        mBtnStartPlay.setOnClickListener(this);
    }

    private void initData() {

        try {
            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_start:
                startPlay();
                break;

            default:
                break;
        }
    }

    private void startPlay() {
        try {
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }
            }

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDisplay(mHolder);
            mMediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener());
            mMediaPlayer.setOnInfoListener(new MediaPlayerInfoListener());

            String path = mEditTextVideoPath.getText().toString();
            if (TextUtils.isEmpty(path)) {
                AssetFileDescriptor afd = getAssets().openFd(PATH);
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            } else {
                mMediaPlayer.setDataSource(path);
            }

            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
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
