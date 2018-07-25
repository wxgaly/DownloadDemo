package nova.android.torrentdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.frostwire.jlibtorrent.TorrentInfo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import nova.android.torrentdemo.core.ITorrentTaskService
import nova.android.torrentdemo.core.TorrentMetaInfo
import nova.android.torrentdemo.service.TorrentTaskService
import java.io.File

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var iTorrentTaskService: ITorrentTaskService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            initData()
        }

        initService()
    }

    private fun initService() {
        val intent = Intent(MainActivity@this, TorrentTaskService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun initData() {

        val filePath = "/sdcard/Download/我不是药神.torrent"
        Flowable.just(filePath)
                .map {
                    iTorrentTaskService?.addTorrentByFilePath(filePath)
                    TorrentInfo(File(it))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
//                    Log.d("wxg", TorrentMetaInfo(it).toString())
                    tv.text = TorrentMetaInfo(it).toString()

                }, onError = {
                    Log.d("wxg", it.message)
                    tv.text = it.message
                })

    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected ----- $name")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected ----- $name")

            service?.let {
                iTorrentTaskService = ITorrentTaskService.Stub.asInterface(it)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        iTorrentTaskService?.pause()
        unbindService(serviceConnection)
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
