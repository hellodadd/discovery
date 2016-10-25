package com.freeme.discovery.http;

import com.freeme.discovery.bean.AppBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by server on 16-10-25.
 */

public interface RequstApkListApi {

    @GET("api/apkinfo")
    Call<AppBean> getApkInfo(@Query("token") String token,
                             @Query("from") String from,
                             @Query("page") int page,
                             @Query("size") int size,
                             @Query("categoryid") int categoryid);
}
