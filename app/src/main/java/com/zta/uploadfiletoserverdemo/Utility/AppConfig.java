package com.zta.uploadfiletoserverdemo.Utility;


import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Zee Abbasi on 10/14/2016.
 */

public class AppConfig {

    public static String BASE_URL = "http://192.168.10.3/";

    public static Retrofit getRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(client)
                .build();
    }
}
