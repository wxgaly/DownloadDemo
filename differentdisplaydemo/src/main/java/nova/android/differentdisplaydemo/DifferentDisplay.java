package nova.android.differentdisplaydemo;

import android.app.Presentation;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.VideoView;

/**
 * nova.android.differentdisplaydemo.
 *
 * @author Created by WXG on 2018/5/8 008 20:08.
 * @version V1.0
 */
public class DifferentDisplay extends Presentation {

    private static final String STREAM_URL = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";
    private static final String VIDEO_NAME = "videoideo.mov";

    private VideoView videoView;

    public DifferentDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_different_display);
        initView();
    }

    private void initView() {
        videoView = findViewById(R.id.videoView);
        String uri = "android.resource://" + getContext().getPackageName() + "/" + R.raw.video;
        videoView.setVideoURI(Uri.parse(uri));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (videoView != null) {
            videoView.start();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("wxg", "onDetachedFromWindow: ");
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
