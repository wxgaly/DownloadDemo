package wxgaly.android.downloaddemo.api;

import wxgaly.android.downloaddemo.bean.FileInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * nova.android.downloaddemo.api.
 *
 * @author Created by WXG on 2018/1/26 026 9:43.
 * @version V1.0
 */

public interface TerminalCloudCallRegulars {

    interface FileDownloadService {

        @GET
        Call<FileInfo> getFileInfo(@Url String url);

        // add Streaming annotation used to download the large files
        @Streaming
        @GET
        Call<ResponseBody> downloadLargeFile(@Url String url);

        // breakpoint resume
        @Streaming
        @GET
        Call<ResponseBody> downloadFromBreakPoint(@Url String url, @Header("RANGE") String Range);

    }

}
