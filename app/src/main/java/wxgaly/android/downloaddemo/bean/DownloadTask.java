package wxgaly.android.downloaddemo.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * nova.android.downloaddemo.bean.
 *
 * @author Created by WXG on 2018/1/26 026 10:17.
 * @version V1.0
 */

public class DownloadTask {

    private String url;
    private String savePath;

    private long totalSize;
    private List<DownloadElement> downloadElements = new ArrayList<>();

    public DownloadTask() {
    }

    public DownloadTask(String url, String savePath, long totalSize) {
        this.url = url;
        this.savePath = savePath;
        this.totalSize = totalSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void addDownloadElement(DownloadElement downloadElement) {
        downloadElements.add(downloadElement);
    }

    public List<DownloadElement> getDownloadElements() {
        return downloadElements;
    }

    public static class DownloadElement {

        private String url;
        private String savePath;

        private long startPosition;
        private long endPosition;
        private long totalSize;

        public DownloadElement(String url, String savePath, long startPosition, long endPosition, long totalSize) {
            this.url = url;
            this.savePath = savePath;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.totalSize = totalSize;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSavePath() {
            return savePath;
        }

        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }

        public long getStartPosition() {
            return startPosition;
        }

        public void setStartPosition(long startPosition) {
            this.startPosition = startPosition;
        }

        public long getEndPosition() {
            return endPosition;
        }

        public void setEndPosition(long endPosition) {
            this.endPosition = endPosition;
        }

        public long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }
    }

}
