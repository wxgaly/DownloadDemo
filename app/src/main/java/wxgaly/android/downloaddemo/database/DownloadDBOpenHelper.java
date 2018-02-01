package wxgaly.android.downloaddemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by WXG on 2017/5/22 022 21:47.
 */

public class DownloadDBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = DownloadDBOpenHelper.class.getSimpleName();

    private static final int VERSIONCODE = 1;

    /**
     * initialzes DBHelper, version of default is 1;
     *
     * @param context
     */
    public DownloadDBOpenHelper(Context context) {
        this(context, DownloadConstant.DOWNLOADINFODBNAME, null, VERSIONCODE);
    }


    public DownloadDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDownloadInfoTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * @Title: createDownloadInfoTable
     * @Description: TODO
     * @param db
     */

    private void createDownloadInfoTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + DownloadConstant.DOWNLOADINFOTABLENAME + " ("
                + DownloadConstant.DOWNLOADINFOID + " integer PRIMARY KEY AUTOINCREMENT, "
                + DownloadConstant.DOWNLOADINFOSRCFILEPATH + " text, "
                + DownloadConstant.DOWNLOADINFOSTARTPOSITION + " integer, "
                + DownloadConstant.DOWNLOADINFOTOTALSIZE + " integer, "
                + DownloadConstant.DOWNLOADINFOFINISHTYPE + " integer)";
        db.execSQL(sql);
        Log.d(TAG, "create DownloadInfo table");
    }
}
