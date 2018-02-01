package wxgaly.android.downloaddemo.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitManager {

    private static volatile RetrofitManager mRetrofitManager = null;
    private static final String BASEURL = "http://www.baidu.com";
    private static final int TIME_OUT = 10;

    private boolean isLog = true;
    private Retrofit mRetrofit;

    private RetrofitManager() {
        initRetrofit();
    }

    public static RetrofitManager getInstance() {
        if (mRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofitManager == null) {

                    mRetrofitManager = new RetrofitManager();
                }
            }
        }

        return mRetrofitManager;
    }

    private void initRetrofit() {

        String baseUrl = BASEURL;

        if (isLog) {
            createLogRetrofit(baseUrl);
        } else {
            createRetrofit(baseUrl);
        }
    }

    private void createRetrofit(String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(TIME_OUT, TimeUnit.SECONDS).build();

        mRetrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client
                (client).build();
    }

    private void createLogRetrofit(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(TIME_OUT, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        mRetrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client
                (client).build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

}
