package wxgaly.android.downloaddemo.database;


import wxgaly.android.downloaddemo.bean.RecordDownloadInfo;

/**
 * nova.android.downloaddemo.database.
 *
 * @author Created by WXG on 2018/1/8 008 16:50.
 * @version V1.0
 */

public interface IDownloadRecorder {

    boolean insertDownloadInfo(RecordDownloadInfo info);

    boolean updateDownloadInfo(RecordDownloadInfo info);

    boolean deleteDownloadInfo(String srcFilePath, int finishType);

    RecordDownloadInfo findDownloadInfo(String srcFilePath, long totalSize, int finishType);

}
