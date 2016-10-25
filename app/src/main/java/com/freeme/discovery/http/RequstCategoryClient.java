package com.freeme.discovery.http;

import com.freeme.discovery.bean.CategoryBean;

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

public class RequstCategoryClient {

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private RequstCategoryApi requstCategoryApi;

    private RequstCategoryClient(){

        OkHttpClient.Builder okhttp = new OkHttpClient().newBuilder();
        okhttp.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(okhttp.build())
                .baseUrl(HttpInterface.CATEGORY_API_BAST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        requstCategoryApi = retrofit.create(RequstCategoryApi.class);
    }

    private static class SingletonHolder{
        private static final RequstCategoryClient INSTANCE = new RequstCategoryClient();
    }

    public static RequstCategoryClient getInstance(){
        return SingletonHolder.INSTANCE;
    }


    public void getCategory(String token, String from, final onRequstCategoryListen listen){

        Call<CategoryBean> getCategory = requstCategoryApi.getCategory(token, from);

        getCategory.enqueue(new Callback<CategoryBean>() {
            @Override
            public void onResponse(Call<CategoryBean> call, Response<CategoryBean> response) {

                if(listen != null){
                    listen.onRequstCategorySuccess(call, response);
                }

            }

            @Override
            public void onFailure(Call<CategoryBean> call, Throwable t) {

                if(listen != null){
                    listen.onRequstCategoryFailure(call, t);
                }

            }
        });

    }


    public interface onRequstCategoryListen{
        void onRequstCategorySuccess(Call<CategoryBean> call, Response<CategoryBean> response);
        void onRequstCategoryFailure(Call<CategoryBean> call, Throwable t);
    }



}
