package nova.android.vlcdemo

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import nova.android.vlcdemo.util.LibVLCUtil
import org.videolan.libvlc.IVLCVout
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.io.File


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val VIDEO_PATH = "${Environment.getExternalStorageDirectory().absolutePath}${File.separator}test.mp4"

    private var mediaPlayer: MediaPlayer? = null
    private var callback: IVLCVout.Callback? = null
    private var vlcVout: IVLCVout? = null
    private var eventListener: MediaPlayer.EventListener? = null

    private var videoWidth: Int = 0
    private var videoHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

    }

    private fun initData() {

        val surfaceHolder = surfaceView.holder
        val libvlc = LibVLCUtil.getLibVLC(null)
        surfaceHolder.setKeepScreenOn(true)
        mediaPlayer = MediaPlayer(libvlc)
        vlcVout = mediaPlayer?.vlcVout

        callback = object : IVLCVout.Callback {
            override fun onSurfacesCreated(p0: IVLCVout?) {
            }

            override fun onNewLayout(p0: IVLCVout?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int) {

                videoWidth = p1
                videoHeight = p2

                val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = windowManager.defaultDisplay
                val point = Point()
                display.getSize(point)

                val layoutParams = surfaceView.layoutParams
                layoutParams.width = point.x
                layoutParams.height = Math.ceil((videoHeight.toFloat() * point.x.toFloat() / videoWidth.toFloat())
                        .toDouble()).toInt()
                surfaceView.layoutParams = layoutParams

            }

            override fun onSurfacesDestroyed(p0: IVLCVout?) {
            }

        }

        vlcVout?.addCallback(callback)
        vlcVout?.setVideoView(surfaceView)
        vlcVout?.attachViews()

        val media = Media(libvlc, VIDEO_PATH)

        mediaPlayer?.media = media

        eventListener = MediaPlayer.EventListener {
            Log.d(TAG, "event: ${it.type}")
        }

        mediaPlayer?.setEventListener(eventListener)

        btn_start.setOnClickListener {
            mediaPlayer?.apply {
                if (isPlaying) {
                    play()
                }
            }

        }

        btn_stop.setOnClickListener {
            mediaPlayer?.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

}
