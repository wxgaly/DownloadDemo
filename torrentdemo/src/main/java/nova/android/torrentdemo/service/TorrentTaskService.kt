package nova.android.torrentdemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import nova.android.torrentdemo.core.TorrentDownload
import nova.android.torrentdemo.core.TorrentEngine
import nova.android.torrentdemo.core.TorrentEngineCallback
import nova.android.torrentdemo.core.stateparcel.BasicStateParcel
import java.lang.Exception

/**
 *  nova.android.torrentdemo.service.
 *
 * @author Created by WXG on 2018/7/24 024 9:54.
 * @version V1.0
 */
class TorrentTaskService : Service(), TorrentEngineCallback {

    private val TAG = "TorrentTaskService"
    private lateinit var iTorrentTaskServiceImpl: ITorrentTaskServiceImpl

    override fun onCreate() {
        super.onCreate()
        iTorrentTaskServiceImpl = ITorrentTaskServiceImpl(applicationContext, TorrentTaskService@ this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return iTorrentTaskServiceImpl
    }

    override fun onDestroy() {
        super.onDestroy()
        iTorrentTaskServiceImpl.cleanTemp()
    }

    override fun onTorrentAdded(id: String?) {
        Log.d(TAG, "onTorrentAdded ---- $id")
    }

    override fun onTorrentStateChanged(id: String?) {
        Log.d(TAG, "onTorrentStateChanged ---- $id")
        sendBasicState(TorrentEngine.getInstance().getTask(id))
        val peerStatesList = iTorrentTaskServiceImpl.getPeerStatesList(id)
        val trackerStatesList = iTorrentTaskServiceImpl.getTrackerStatesList(id)

//        peerStatesList?.forEach {
//            Log.d(TAG, "peerStatesList : ------ $it")
//        }

//        trackerStatesList?.forEach {
//            Log.d(TAG, "trackerStatesList: ------- $it")
//        }

    }

    override fun onTorrentFinished(id: String?) {
        Log.d(TAG, "onTorrentFinished ---- $id")
    }

    override fun onTorrentRemoved(id: String?) {
        Log.d(TAG, "onTorrentRemoved ---- $id")
    }

    override fun onTorrentPaused(id: String?) {
        Log.d(TAG, "onTorrentPaused ---- $id")
    }

    override fun onTorrentResumed(id: String?) {
        Log.d(TAG, "onTorrentResumed ---- $id")
    }

    override fun onEngineStarted() {
        Log.d(TAG, "onEngineStarted --")
    }

    override fun onTorrentMoved(id: String?, success: Boolean) {
        Log.d(TAG, "onTorrentMoved ---- $id")
    }

    override fun onIpFilterParsed(success: Boolean) {
        Log.d(TAG, "onIpFilterParsed -- $success")
    }

    override fun onMagnetLoaded(hash: String?, bencode: ByteArray?) {
        Log.d(TAG, "onMagnetLoaded ---- $hash")
    }

    override fun onTorrentMetadataLoaded(id: String?, err: Exception?) {
        Log.d(TAG, "onTorrentMetadataLoaded ---- $id")
    }

    override fun onRestoreSessionError(id: String?) {
        Log.d(TAG, "onRestoreSessionError ---- $id")
    }

    private fun sendBasicState(task: TorrentDownload?) {
        if (task == null)
            return
//        val torrent = task.torrent ?: return

        val state = makeBasicStateParcel(task)
        Log.d(TAG, state.toString())

    }

    private fun makeBasicStateParcel(task: TorrentDownload?): BasicStateParcel? {
        if (task == null)
            return null

        val torrent = task.torrent

        return BasicStateParcel(
                torrent.id,
                torrent.name,
                task.stateCode,
                task.progress,
                task.totalReceivedBytes,
                task.totalSentBytes,
                task.totalWanted,
                task.downloadSpeed,
                task.uploadSpeed,
                task.eta,
                torrent.dateAdded,
                task.totalPeers,
                task.connectedPeers)
    }

}