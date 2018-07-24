// ITorrentTaskService.aidl
package nova.android.torrentdemo.core;

// Declare any non-default types here with import statements
import nova.android.torrentdemo.core.AddTorrentParams;

interface ITorrentTaskService {

    void addTorrentByFilePath(String filePath);

    void addTorrent(in AddTorrentParams params, boolean removeFile);
}
