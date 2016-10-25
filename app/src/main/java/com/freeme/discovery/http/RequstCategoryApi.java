package com.freeme.discovery.http;

import com.freeme.discovery.bean.CategoryBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by server on 16-10-25.
 */

public interface RequstCategoryApi {

    @GET("api/broad")
    Call<CategoryBean> getCategory(@Query("token") String token,
                                   @Query("from") String from);
}
