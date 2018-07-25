package nova.android.torrentdemo.service

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.frostwire.jlibtorrent.Priority
import com.frostwire.jlibtorrent.TorrentInfo
import nova.android.torrentdemo.core.*
import nova.android.torrentdemo.core.stateparcel.PeerStateParcel
import nova.android.torrentdemo.core.stateparcel.TrackerStateParcel
import nova.android.torrentdemo.core.storage.TorrentStorage
import nova.android.torrentdemo.core.utils.FileIOUtils
import nova.android.torrentdemo.core.utils.TorrentUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

/**
 *  nova.android.torrentdemo.service.
 *
 * @author Created by WXG on 2018/7/24 024 9:56.
 * @version V1.0
 */
class ITorrentTaskServiceImpl(val context: Context, callback: TorrentEngineCallback?) : ITorrentTaskService.Stub() {

    private val TAG = "ITorrentTaskServiceImpl"
    private var repo: TorrentStorage = TorrentStorage(context)

    init {
        TorrentEngine.getInstance().setContext(context)
        TorrentEngine.getInstance().setCallback(callback)
        TorrentEngine.getInstance().start()
    }

    override fun addTorrentByFilePath(filePath: String?) {

        if (TextUtils.isEmpty(filePath)) {
            return
        }

        val file = File(filePath)
        val ti: TorrentInfo

        try {
            ti = TorrentInfo(file)
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, "Failed decoding torrent file. Maybe the file is corrupted or has an incorrect format")
            return
        }

        Log.d(TAG, TorrentMetaInfo(ti).toString())
        val priorities = ArrayList(Collections.nCopies(ti.files().numFiles(), Priority.NORMAL))
        val downloadPath = FileIOUtils.getDefaultDownloadPath()
        val params = AddTorrentParams(file.absolutePath, false, ti.infoHash().toHex(),
                ti.name(), priorities, downloadPath, false, false)
        if (isAllFilesTooBig(downloadPath, ti)) {
            Log.d(TAG, "There is not enough free space on the selected path to download the selected files")
            return
        }

        try {
            addTorrent(params, false)
        } catch (e: Throwable) {
            Log.e(TAG, Log.getStackTraceString(e))
        }

    }


    override fun addTorrent(params: AddTorrentParams?, removeFile: Boolean) {

        if (params == null)
            return

        var torrent = Torrent(params.sha1hash, params.name, params.filePriorities,
                params.pathToDownload, System.currentTimeMillis())
        torrent.torrentFilePath = params.source
        torrent.isSequentialDownload = params.isSequentialDownload
        torrent.isPaused = params.addPaused()

        if (params.fromMagnet()) {
            val bencode = TorrentEngine.getInstance().getLoadedMagnet(torrent.id)
            TorrentEngine.getInstance().removeLoadedMagnet(torrent.id)
            if (!repo.exists(torrent)) {
                if (bencode == null) {
                    torrent.isDownloadingMetadata = true
                    repo.add(torrent)
                } else {
                    torrent.isDownloadingMetadata = false
                    repo.add(torrent, bencode)
                }
            }
        } else if (File(torrent.torrentFilePath).exists()) {
            if (repo.exists(torrent)) {
                repo.replace(torrent, removeFile)
//                throw FileAlreadyExistsException(File(torrent.torrentFilePath))
            } else {
                repo.add(torrent, torrent.torrentFilePath, removeFile)
            }
        } else {
            throw FileNotFoundException(torrent.torrentFilePath)
        }
        torrent = repo.getTorrentByID(torrent.id)
        if (torrent == null)
            throw IOException("torrent is null")
//        if (!torrent.isDownloadingMetadata) {
//            if (pref.getBoolean(getString(R.string.pref_key_save_torrent_files),
//                            SettingsManager.Default.saveTorrentFiles))
//                saveTorrentFileIn(torrent!!, pref.getString(getString(R.string.pref_key_save_torrent_files_in),
//                        torrent!!.getDownloadPath()))
//        }
        if (!torrent.isDownloadingMetadata && !TorrentUtils.torrentDataExists(context, torrent.id)) {
            repo.delete(torrent)
            throw FileNotFoundException("Torrent doesn't exists: " + torrent.name)
        }

        TorrentEngine.getInstance().download(torrent)

    }

    fun cleanTemp() {
        try {
            FileIOUtils.cleanTempDir(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error during setup of temp directory: ", e)
        }

    }

    override fun pause() {
        TorrentEngine.getInstance().pause()
    }

    private fun isAllFilesTooBig(downloadPath: String, ti: TorrentInfo): Boolean {
        val space = FileIOUtils.getFreeSpace(downloadPath)
        val total = ti.totalSize()
        Log.d(TAG, "space : $space --------  total : $total")
        return FileIOUtils.getFreeSpace(downloadPath) < ti.totalSize()
    }

    fun getPeerStatesList(id: String?): ArrayList<PeerStateParcel>? {
        if (id == null)
            return null

        val task = TorrentEngine.getInstance().getTask(id) ?: return null

        return makePeerStateParcelList(task)
    }

    fun getTrackerStatesList(id: String?): ArrayList<TrackerStateParcel>? {
        if (id == null)
            return null

        val task = TorrentEngine.getInstance().getTask(id) ?: return null

        return makeTrackerStateParcelList(task)
    }

    private fun makeTrackerStateParcelList(task: TorrentDownload?): ArrayList<TrackerStateParcel>? {
        if (task == null)
            return null

        val trackers = task.trackers
        val states = ArrayList<TrackerStateParcel>()

        val statusDHT = if (TorrentEngine.getInstance().isDHTEnabled)
            TrackerStateParcel.Status.WORKING
        else
            TrackerStateParcel.Status.NOT_WORKING

        val statusLSD = if (TorrentEngine.getInstance().isLSDEnabled)
            TrackerStateParcel.Status.WORKING
        else
            TrackerStateParcel.Status.NOT_WORKING

        val statusPeX = if (TorrentEngine.getInstance().isPeXEnabled)
            TrackerStateParcel.Status.WORKING
        else
            TrackerStateParcel.Status.NOT_WORKING

        states.add(TrackerStateParcel(TrackerStateParcel.DHT_ENTRY_NAME, "", -1, statusDHT))
        states.add(TrackerStateParcel(TrackerStateParcel.LSD_ENTRY_NAME, "", -1, statusLSD))
        states.add(TrackerStateParcel(TrackerStateParcel.PEX_ENTRY_NAME, "", -1, statusPeX))

        for (entry in trackers) {
            val url = entry.url()
            /* Prevent duplicate */
            if (url == TrackerStateParcel.DHT_ENTRY_NAME ||
                    url == TrackerStateParcel.LSD_ENTRY_NAME ||
                    url == TrackerStateParcel.PEX_ENTRY_NAME) {
                continue
            }

            states.add(TrackerStateParcel(entry.swig()))
        }

        return states
    }

    private fun makePeerStateParcelList(task: TorrentDownload?): ArrayList<PeerStateParcel>? {
        if (task == null) {
            return null
        }

        val states = ArrayList<PeerStateParcel>()
        val peers = task.peers

        val status = task.torrentStatus

        for (peer in peers) {
            val state = PeerStateParcel(peer.swig(), status)
            states.add(state)
        }

        return states
    }

}