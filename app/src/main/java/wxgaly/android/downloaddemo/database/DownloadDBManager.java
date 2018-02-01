package wxgaly.android.downloaddemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import wxgaly.android.downloaddemo.bean.RecordDownloadInfo;

/**
 * Created by WXG on 2017/5/23 023 9:04.
 */

public class DownloadDBManager implements IDownloadRecorder {

    private static final String TAG = DownloadDBManager.class.getSimpleName();

    private Context mContext;

    private static DownloadDBManager mDownloadDBManager;

    private DownloadDBManager(Context context) {
        this.mContext = context;
    }

    /**
     * @param context
     * @return
     * @Title: newInstance
     * @Description: TODO
     */
    public static DownloadDBManager newInstance(Context context) {
        if (mDownloadDBManager == null) {
            synchronized (DownloadDBManager.class) {
                if (mDownloadDBManager == null) {
                    mDownloadDBManager = new DownloadDBManager(context);
                }
            }
        }
        return mDownloadDBManager;
    }

    /**
     * @param info
     * @Title: insertDownloadInfo
     * @Description: TODO
     */
    public synchronized boolean insertDownloadInfo(RecordDownloadInfo info) {
        if (info != null) {

            DownloadDBOpenHelper helper = new DownloadDBOpenHelper(mContext);

            SQLiteDatabase writableDatabase = null;

            ContentValues values = null;
            try {
                writableDatabase = helper.getWritableDatabase();
                values = new ContentValues();
                values.put(DownloadConstant.DOWNLOADINFOSRCFILEPATH, info.getSrcFilePath());
                values.put(DownloadConstant.DOWNLOADINFOSTARTPOSITION, info.getStartPosition());
                values.put(DownloadConstant.DOWNLOADINFOTOTALSIZE, info.getTotalSize());
                values.put(DownloadConstant.DOWNLOADINFOFINISHTYPE, info.getFinishType());

                writableDatabase.insert(DownloadConstant.DOWNLOADINFOTABLENAME, null, values);
            } catch (Exception e) {
                Log.e(TAG, "insertDownloadInfo: " + e.getMessage(), e);
            } finally {
                if (writableDatabase != null) {
                    writableDatabase.close();
                }
            }

            return true;

        } else {
            return false;
        }
    }

    /**
     * @param info
     * @Title: updateDownloadInfo
     * @Description: TODO
     */
    public synchronized boolean updateDownloadInfo(RecordDownloadInfo info) {
        if (info != null) {
            int ret = -1;

            DownloadDBOpenHelper helper = new DownloadDBOpenHelper(mContext);

            SQLiteDatabase writableDatabase = null;
            ContentValues values = null;

            try {
                writableDatabase = helper.getWritableDatabase();

                values = new ContentValues();

                values.put(DownloadConstant.DOWNLOADINFOSTARTPOSITION, info.getStartPosition());

                String where = DownloadConstant.DOWNLOADINFOSRCFILEPATH
                        + "=? and "
                        + DownloadConstant.DOWNLOADINFOTOTALSIZE
                        + "=?";
                String[] selectionArgs = new String[]{info.getSrcFilePath()};
                ret = writableDatabase.update(DownloadConstant.DOWNLOADINFOTABLENAME, values, where, selectionArgs);

            } catch (Exception e) {
                Log.e(TAG, "updateDownloadInfo: " + e.getMessage(), e);
                return false;
            } finally {
                if (writableDatabase != null) {
                    writableDatabase.close();
                }
            }

            return ret > 0;

        } else {
            return false;
        }
    }

    /**
     * @Title: deleteDownloadInfo
     * @Description: TODO
     */
    public synchronized boolean deleteDownloadInfo(String srcFilePath, int finishType) {


        int ret = -1;

        DownloadDBOpenHelper helper = new DownloadDBOpenHelper(mContext);

        SQLiteDatabase writableDatabase = null;

        try {

            writableDatabase = helper.getWritableDatabase();

            String where = DownloadConstant.DOWNLOADINFOSRCFILEPATH + "=? and "
                    + DownloadConstant.DOWNLOADINFOFINISHTYPE + "=?";
            String[] selectionArgs = new String[]{srcFilePath, String.valueOf(finishType)};
            ret = writableDatabase.delete(DownloadConstant.DOWNLOADINFOTABLENAME, where, selectionArgs);

        } catch (Exception e) {
            Log.e(TAG, "deleteDownloadInfo: " + e.getMessage(), e);
        } finally {

            if (writableDatabase != null) {
                writableDatabase.close();
            }
        }

        return ret > 0;

    }

    /**
     * @return
     * @Title: findDownloadInfo
     * @Description: TODO
     */
    public synchronized RecordDownloadInfo findDownloadInfo(String srcFilePath, long totalSize, int finishType) {


        DownloadDBOpenHelper helper = new DownloadDBOpenHelper(mContext);

        SQLiteDatabase readableDatabase = null;

        Cursor cursor = null;

        RecordDownloadInfo downloadInfo = null;

        try {

            readableDatabase = helper.getReadableDatabase();

            String[] projection = new String[]{DownloadConstant.DOWNLOADINFOID,
                    DownloadConstant.DOWNLOADINFOSRCFILEPATH, DownloadConstant.DOWNLOADINFOSTARTPOSITION,
                    DownloadConstant.DOWNLOADINFOTOTALSIZE, DownloadConstant.DOWNLOADINFOFINISHTYPE};

            String selection = DownloadConstant.DOWNLOADINFOSRCFILEPATH + "=? and " + DownloadConstant
                    .DOWNLOADINFOFINISHTYPE + "=?";

            String[] selectionArgs = new String[]{srcFilePath, String.valueOf(finishType)};

            cursor = readableDatabase.query(DownloadConstant.DOWNLOADINFOTABLENAME, projection, selection,
                    selectionArgs, null, null, null);


            if (cursor != null && cursor.moveToFirst()) {

                int id = cursor.getInt(cursor.getColumnIndex(DownloadConstant.DOWNLOADINFOID));
                String srcPath = cursor.getString(cursor.getColumnIndex(DownloadConstant.DOWNLOADINFOSRCFILEPATH));
                int startPosition = cursor.getInt(cursor.getColumnIndex(DownloadConstant
                        .DOWNLOADINFOSTARTPOSITION));
                totalSize = cursor.getInt(cursor.getColumnIndex(DownloadConstant.DOWNLOADINFOTOTALSIZE));
                finishType = cursor.getInt(cursor.getColumnIndex(DownloadConstant.DOWNLOADINFOFINISHTYPE));

                downloadInfo = new RecordDownloadInfo(id, srcPath, startPosition, totalSize, finishType);
                cursor.close();

            }

        } catch (Exception e) {
            Log.e(TAG, "findDownloadInfo: " + e.getMessage(), e);
        } finally {

            if (cursor != null) {
                cursor.close();
            }

            if (readableDatabase != null) {
                readableDatabase.close();
            }

        }

        return downloadInfo;

    }


}
