package wxgaly.android.downloaddemo.bean;

public interface IDownloadCallback {

    interface IDownloadProgress {
        public void onDownloadProgress(int id, long downloadSize, long blockSize);
    }

}
