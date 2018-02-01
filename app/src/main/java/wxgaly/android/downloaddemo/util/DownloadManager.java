package wxgaly.android.downloaddemo.util;

import wxgaly.android.downloaddemo.bean.DownloadTask;

import java.util.ArrayList;
import java.util.List;

public class DownloadManager {

    private static volatile DownloadManager mDownloadManager = null;

    private static final int THREAD_COUNT = 2;
    private Builder builder;
    private List<DownloadUtils> downloadUtilsList;

    private DownloadManager() {
        downloadUtilsList = new ArrayList<>();
    }

    public static DownloadManager getInstance() {

        if (mDownloadManager == null) {
            synchronized (DownloadManager.class) {
                if (mDownloadManager == null) {
                    mDownloadManager = new DownloadManager();
                }
            }
        }
        return mDownloadManager;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;

        List<DownloadTask> downloadTasks = builder.getDownloadTasks();
        for (DownloadTask downloadTask : downloadTasks) {
            checkDownloadTask(downloadTask);
        }
    }

    private void checkDownloadTask(DownloadTask downloadTask) {

    }

    public void start() {

    }

    public void stop() {

    }

    class Builder {

        private long notifyProgressValue = 1024 * 1024;
        private int threadCount = THREAD_COUNT;
        private List<DownloadTask> downloadTasks = new ArrayList<>();
        private DownloadManager downloadManager = DownloadManager.getInstance();

        public Builder setThreadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        public Builder setNotifyProgressValue(long progressValue) {
            this.notifyProgressValue = progressValue;
            return this;
        }

        public Builder addDownloadTask(DownloadTask downloadTask) {
            if (downloadTask != null) {
                downloadTasks.add(downloadTask);
            }
            return this;
        }

        public Builder build() {
            downloadManager.setBuilder(this);
            return this;
        }

        public List<DownloadTask> getDownloadTasks() {
            return downloadTasks;
        }

        public long getNotifyProgressValue() {
            return notifyProgressValue;
        }

        public int getThreadCount() {
            return threadCount;
        }


    }
}
