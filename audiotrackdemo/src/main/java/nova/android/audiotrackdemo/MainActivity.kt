package nova.android.audiotrackdemo

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val data = byteArrayOf(
            0,
            0x01,
            0xc0.toByte(),
            0x01,
            0xc0.toByte(),
            0x01,
            0xc0.toByte(),
            0x01,
            0xc0.toByte(),
            0x01,
            0xc0.toByte(),
            0x01,
            0xc0.toByte(),
            0x01,
            0xc0.toByte(),
            0x01,
            0xc0.toByte(),
            0,
            0,
            0x01,
            0xbd.toByte(),
            0x01,
            0xd2.toByte(),
            0x01,
            0xc0.toByte(),
            0x01,
            0xc1.toByte(),
            0x01,
            0xc4.toByte(),
            0x01,
            0xc2.toByte(),
            0x01,
            0xc8.toByte(),
            0x01,
            0xc0.toByte(),
            0
    )

    var audioTrack: AudioTrack? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val bufferSize = AudioTrack.getMinBufferSize(44100,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)
            audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize, AudioTrack.MODE_STATIC)
            audioTrack?.write(data, 0, data.size)
            audioTrack?.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioTrack?.apply {
            stop()
            release()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
