package wxgaly.android.downloaddemo.database;

/**
 * Created by WXG on 2017/5/22 022 21:48.
 */

public class DownloadConstant {

    public static final String DOWNLOADINFODBNAME = "download_info.db";

    public static final String DOWNLOADINFOTABLENAME = "download_info_db";

    public static final String DOWNLOADINFOID = "id";
    public static final String DOWNLOADINFOSRCFILEPATH = "srcfilepath";
    public static final String DOWNLOADINFOSELFFILEPATH = "selffilepath";
    public static final String DOWNLOADINFOSTARTPOSITION = "startposition";
    public static final String DOWNLOADINFOTOTALSIZE = "totalsize";
    public static final String DOWNLOADINFOFINISHTYPE = "finishtype";

    public static final int DOWNLOADING = 0;
    public static final int DOWNLOAD_COMPLETE = 1;
    public static final int DOWNLOAD_FAILED = 2;

    public static final int UPDATE_START = 0; //开始升级
    public static final int UPDATE_COMPLETE = 1; //升级完成
    public static final int UPDATE_FAILED = 2; //升级失败



    public static final long REPORT_PROGRESS_INTERVAL = 5*1000;


    public static final int UPDATE_NOTDOWNLOAD = 0x00;
    public static final int UPDATE_DOWNLOADING = 0x01;
    public static final int UPDATE_DOWNLOADFINISH = 0x02;
    public static final int UPDATE_UPDATING = 0x03;
}
