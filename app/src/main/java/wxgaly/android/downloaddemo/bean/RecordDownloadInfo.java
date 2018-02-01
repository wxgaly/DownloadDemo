/*
 *  @Title DownloadInfo.java 
 *  classes : nova.android.downloaddemo1.download . DownloadInfo
 *  @Description     
 *  @author WangXuGuang
 *  @date Create  at  Oct 25, 2016  3:18:46 PM
 *  version  [V 1.0.0]
 */
package wxgaly.android.downloaddemo.bean;

/**
 * nova.android.downloaddemo1.download.DownloadInfo
 *
 * @author WangXuGuang <br/>
 * @brief
 * @date create at Oct 25, 2016 3:18:46 PM
 */
public class RecordDownloadInfo {

    private int id;
    private String srcFilePath;
    private String selfFilePath;
    private long startPosition;
    private long totalSize;
    private int finishType;
    private String url;

    public RecordDownloadInfo(String srcFilePath, long startPosition, long totalSize, int finishType) {
        super();
        this.srcFilePath = srcFilePath;
        this.startPosition = startPosition;
        this.totalSize = totalSize;
        this.finishType = finishType;
    }

    public RecordDownloadInfo(int id, String srcFilePath, long startPosition, long totalSize, int finishType) {
        super();
        this.id = id;
        this.srcFilePath = srcFilePath;
        this.startPosition = startPosition;
        this.totalSize = totalSize;
        this.finishType = finishType;
    }

    public RecordDownloadInfo(int id, String srcFilePath, String selfFilePath, long startPosition, long totalSize,
                              int finishType) {
        super();
        this.id = id;
        this.srcFilePath = srcFilePath;
        this.selfFilePath = selfFilePath;
        this.startPosition = startPosition;
        this.totalSize = totalSize;
        this.finishType = finishType;
    }

    public RecordDownloadInfo(int id, String srcFilePath, String selfFilePath, int startPosition, int totalSize,
                              int finishType, String url) {
        super();
        this.id = id;
        this.srcFilePath = srcFilePath;
        this.selfFilePath = selfFilePath;
        this.startPosition = startPosition;
        this.totalSize = totalSize;
        this.finishType = finishType;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrcFilePath() {
        return srcFilePath;
    }

    public void setSrcFilePath(String srcFilePath) {
        this.srcFilePath = srcFilePath;
    }

    public String getSelfFilePath() {
        return selfFilePath;
    }

    public void setSelfFilePath(String selfFilePath) {
        this.selfFilePath = selfFilePath;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getFinishType() {
        return finishType;
    }

    public void setFinishType(int finishType) {
        this.finishType = finishType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
