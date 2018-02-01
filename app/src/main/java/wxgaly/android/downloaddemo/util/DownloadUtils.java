package wxgaly.android.downloaddemo.util;

import android.util.Log;
import wxgaly.android.downloaddemo.api.TerminalCloudCallRegulars;
import wxgaly.android.downloaddemo.bean.DownloadTask;
import wxgaly.android.downloaddemo.bean.FileInfo;
import wxgaly.android.downloaddemo.bean.IDownloadCallback;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * nova.android.downloaddemo.
 * <p>
 * the download util can download little file or larger file,
 * and support multi thread to download.
 *
 * @author Created by WXG on 2018/1/9 009 11:09.
 * @version V1.0
 */

public class DownloadUtils {

    private static final String TAG = DownloadUtils.class.getSimpleName();
    private static final String BASEURL = "http://www.baidu.com";
    private static final int BUFF_SIZE = 2 * 1024;
    //    private static final int SEGMENT_SIZE = 4 * 1024 * 1024;
    private static final int SEGMENT_SIZE = 4 * 1024 * 1024;
    private static final int THREAD_COUNT = 2;
    private ThreadPoolExecutor mThreadPoolExecutor;

    private Retrofit mRetrofit;
    private static volatile boolean isStart = true;

    private IDownloadCallback.IDownloadProgress downloadProgress;
    private long notifyProgressValue = 1024 * 1024;
    private int threadCount = THREAD_COUNT;

    /**
     *
     */
    public DownloadUtils() {
        mThreadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        initRetrofit();
    }

    /**
     * init retrofit
     */
    private void initRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder().baseUrl(BASEURL).client(client).callbackExecutor(mThreadPoolExecutor).build();
    }

    /**
     * start download by one thread.
     *
     * @param url      the download url.
     * @param savePath the local saved path.
     */
    public void start(String url, String savePath) {
        DownloadThread thread = new DownloadThread(0, url, savePath);
        mThreadPoolExecutor.execute(thread);
    }

    /**
     * start download by multi thread.
     *
     * @param downloadTask the download task.
     */
    public void startMultiThread(DownloadTask downloadTask) {

        List<DownloadTask.DownloadElement> downloadTasks = downloadTask.getDownloadElements();
        if (!downloadTasks.isEmpty()) {
            startMultiThread(downloadTask.getUrl(), downloadTask.getSavePath(), downloadTask.getTotalSize(),
                    downloadTasks);
        }
    }

    /**
     * start download by multi thread.
     *
     * @param url              the download url.
     * @param savePath         the local saved path.
     * @param size             the download file size.
     * @param downloadElements download elements.
     */
    public void startMultiThread(String url, String savePath, long size, List<DownloadTask.DownloadElement> downloadElements) {
        this.threadCount = downloadElements.size();
        allocateThread(url, savePath, downloadElements);
    }

    /**
     * start download by multi thread.
     *
     * @param url         the download url.
     * @param savePath    the local saved path.
     * @param size        the download file size.
     * @param threadCount the download thread.
     */
    public void startMultiThread(String url, String savePath, long size, int threadCount) {
        this.threadCount = threadCount;
        allocateThread(url, savePath, size);
    }

    /**
     * start download by multi thread.
     *
     * @param url         the download url.
     * @param savePath    the local saved path.
     * @param body        {@link FileInfo} thr file info.
     * @param threadCount the download thread.
     */
    public void startMultiThread(String url, String savePath, FileInfo body, int threadCount) {
        this.threadCount = threadCount;
        if (body != null) {
            long size = body.getFileSize();
            allocateThread(url, savePath, size);
        }
    }

    /**
     * stop the current download tasks.
     */
    public void stopDownload() {
        if (mThreadPoolExecutor != null) {
            List<Runnable> runnableList = mThreadPoolExecutor.shutdownNow();
            isStart = false;
        }
    }

    /**
     * set the callback of download progress.
     *
     * @param downloadProgress {@link IDownloadCallback.IDownloadProgress} the
     *                         callback of IDownloadProgress
     */
    public void setDownloadProgress(IDownloadCallback.IDownloadProgress downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    /**
     * get the value of notify progress.
     *
     * @return the value of notify progress.
     */
    public long getNotifyProgressValue() {
        return notifyProgressValue;
    }

    /**
     * set the value of notify progress.
     *
     * @param notifyProgressValue the value of notify progress.
     */
    public void setNotifyProgressValue(long notifyProgressValue) {
        this.notifyProgressValue = notifyProgressValue;
    }

    /**
     * allocate the count of downloadElements thread to download.
     *
     * @param url              the download url.
     * @param savePath         the local saved path.
     * @param downloadElements download elements.
     */
    private void allocateThread(String url, String savePath, List<DownloadTask.DownloadElement> downloadElements) {

        for (int i = 0; i < downloadElements.size(); i++) {
            DownloadTask.DownloadElement downloadElement = downloadElements.get(i);
            mThreadPoolExecutor.execute(new DownloadThread(i, url, savePath, downloadElement.getStartPosition(),
                    downloadElement.getEndPosition()));
        }

    }

    /**
     * allocate the specified count thread to download.
     *
     * @param url      the download url.
     * @param savePath the local saved path.
     * @param size     the download file size.
     */
    private void allocateThread(String url, String savePath, long size) {

        long segmentSize = size / threadCount;

        for (int i = 0; i < threadCount; i++) {

            long startPosition = i * segmentSize;
            long endPosition = (i + 1) * segmentSize;

            if (i == 0) {
                startPosition = 0;
            } else if (i == threadCount - 1) {
                endPosition = size;
            }

            mThreadPoolExecutor.execute(new DownloadThread(i, url, savePath, startPosition, endPosition));
        }

    }

    /**
     * the class download thread.
     * support the range download.
     */
    public class DownloadThread extends Thread {

        /**
         * thread id.
         */
        private int id;

        /**
         * download url.
         */
        private String url;

        /**
         * save path.
         */
        private String savePath;

        /**
         * start position.
         */
        private long startPosition;

        /**
         * end position.
         */
        private long endPosition;

        public DownloadThread(int id, String url, String savePath) {
            this.id = id;
            this.url = url;
            this.savePath = savePath;
        }

        public DownloadThread(int id, String url, String savePath, long startPosition, long endPosition) {
            this.id = id;
            this.url = url;
            this.savePath = savePath;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        @Override
        public void run() {
            startBreakDownload(id, url, savePath, startPosition, endPosition);
//                startBreakDownload(url, savePath, 30 * 1024 * 1024 + 1, 60 * 1024 * 1024);
//                startBreakDownload(url, savePath, 60 * 1024 * 1024 + 1, 90 * 1024 * 1024);
//                startBreakDownload(url, savePath, 90 * 1024 * 1024 + 1, 130 * 1024 * 1024);
        }
    }

    private void startBreakDownload(int id, String url, final String savePath, long startPosition, long endPosition) {
        TerminalCloudCallRegulars.FileDownloadService service = mRetrofit.create(TerminalCloudCallRegulars.FileDownloadService.class);

        String Range = "bytes=" + startPosition + "-" + endPosition;
        Log.d(TAG, "Range: " + Range);
        Call<ResponseBody> call = service.downloadFromBreakPoint(url, Range);

        enqueue(id, savePath, call, startPosition, endPosition);
    }

    private void enqueue(final int id, final String savePath, Call<ResponseBody> call, final long startPosition,
                         final long endPosition) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse is success");
                        breakPointWriteToDisk(id, savePath, response.body(), startPosition, endPosition);
                    } else {
                        Log.w(TAG, "onResponse is failure");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage(), e);
                    e.printStackTrace();
                }

                Log.d(TAG, "end: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
            }
        });
    }

    private boolean breakPointWriteToDisk(int id, String savePath, ResponseBody body, long startPosition, long endPosition) {
        try {
            File downFile = new File(savePath);
            if (downFile.exists()) {
                downFile.delete();
            }
            if (!downFile.getParentFile().exists()) {
                downFile.getParentFile().mkdirs();
            }
            downFile.createNewFile();

            InputStream inputStream = null;
            RandomAccessFile randomAccessFile = null;

            try {
                byte[] fileReader = new byte[BUFF_SIZE];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                long downloaded = 0;
                long blockSize = endPosition - startPosition;

                inputStream = body.byteStream();
                randomAccessFile = new RandomAccessFile(downFile, "rw");

                randomAccessFile.seek(startPosition);
                int len = 0;
                while (isStart) {

                    if ((len = inputStream.read(fileReader)) == -1) {
                        break;
                    }

                    randomAccessFile.write(fileReader, 0, len);

                    fileSizeDownloaded += len;
                    downloaded += len;

                    if (downloaded > SEGMENT_SIZE) {
                        Log.d("wxg",
                                getFileName(savePath) + " break download: "
                                        + (fileSizeDownloaded / (long) (1024 * 1024)) + "M of "
                                        + (fileSize / (long) (1024 * 1024)) + "M");
                        synchronized (DownloadThread.class) {
                            if (downloadProgress != null) {
                                downloadProgress.onDownloadProgress(id, fileSizeDownloaded, blockSize);
                            }
                        }
                        downloaded = 0;
                    } else if (downloaded > notifyProgressValue) {
                        synchronized (DownloadThread.class) {
                            if (downloadProgress != null) {
                                downloadProgress.onDownloadProgress(id, fileSizeDownloaded, blockSize);
                            }
                        }

                        downloaded = 0;
                    } else if (fileSizeDownloaded >= blockSize) {
                        synchronized (DownloadThread.class) {
                            if (downloadProgress != null) {
                                downloadProgress.onDownloadProgress(id, blockSize, blockSize);
                            }
                        }
                    }
                }
                return isStart;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }

            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param urlPath (url)
     * @return
     * @Title: getFileName
     * @Description: split url, return the last of part of url, (eg:
     * http://aa/b.txt, return b.txt)
     */
    public static String getFileName(String urlPath) {

        return urlPath.substring(urlPath.lastIndexOf("/") + 1);
    }

}
