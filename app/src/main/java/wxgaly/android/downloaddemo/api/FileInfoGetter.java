package wxgaly.android.downloaddemo.api;

import wxgaly.android.downloaddemo.bean.FileInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class FileInfoGetter {

    public void getFileInfo(String url, Callback<FileInfo> callback) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        TerminalCloudCallRegulars.FileDownloadService service = retrofit.create(TerminalCloudCallRegulars.FileDownloadService.class);

        Call<FileInfo> call = service.getFileInfo(url);

        call.enqueue(callback);
    }

}
