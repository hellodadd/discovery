package com.freeme.discovery.http;

import com.freeme.discovery.bean.CategoryBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by server on 16-10-31.
 */

public interface TestVideoSerivce {
    @GET("api")
    Call<TestVideoApi> getTestVideo(@Query("cid") int cid);
}
