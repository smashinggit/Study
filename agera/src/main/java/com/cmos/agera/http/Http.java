package com.cmos.agera.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.drakeet.retrofit2.adapter.agera.AgeraCallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * author : ChenSen
 * data : 2019/5/24
 * desc:
 */
public class Http {

    public static String BASE_URL = "https://www.wanandroid.com/";
    public static String IMAGE_URL = "http://dingyue.ws.126.net/S8bZlsJBtFYOf0xrTJfjPrpIPVtjL5MawSVqYPkey3KCd1548725430403compressflag.jpg";
    public static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(3);


    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();


    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(FastJsonConverterFactory.create())
            .addCallAdapterFactory(AgeraCallAdapterFactory.create())
            .client(client)
            .baseUrl(BASE_URL);


    public static <T> T createService(Class<T> service) {
        return builder.build().create(service);
    }

}
