package com.freeme.discovery.http;

import com.freeme.discovery.bean.AppBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by server on 16-10-25.
 */

public class RequstApkListClient {

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private RequstApkListApi requstApkListApi;

    private RequstApkListClient(){

        OkHttpClient.Builder okhttp = new OkHttpClient().newBuilder();
        okhttp.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(okhttp.build())
                .baseUrl(HttpInterface.APK_API_BAST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        requstApkListApi = retrofit.create(RequstApkListApi.class);
    }

    private static class SingletonHolder{
        private static final RequstApkListClient INSTANCE = new RequstApkListClient();
    }

    public static RequstApkListClient getInstance(){
        return RequstApkListClient.SingletonHolder.INSTANCE;
    }



    public void getApkInfo(String token, String from, int page, int size, int categoryid,
                           final onRequstApkListen listen){

        Call<AppBean> getApkInfo = requstApkListApi.getApkInfo(token, from, page, size, categoryid);

        getApkInfo.enqueue(new Callback<AppBean>() {
            @Override
            public void onResponse(Call<AppBean> call, Response<AppBean> response) {

                if(listen != null){
                    listen.onRequstApkSuccess(call, response);
                }

            }

            @Override
            public void onFailure(Call<AppBean> call, Throwable t) {

                if(listen != null){
                    listen.onRequstApkFailure(call, t);
                }

            }
        });

    }


    public interface onRequstApkListen{
        void onRequstApkSuccess(Call<AppBean> call, Response<AppBean> response);
        void onRequstApkFailure(Call<AppBean> call, Throwable t);
    }
}
