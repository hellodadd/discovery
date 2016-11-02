package com.freeme.discovery.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by server on 16-11-2.
 */

public interface TestShopService {
    @GET("wxnew")
    Call<TestShopApi> getShopInfo(@Query("key") String key,
                                  @Query("num") int num);
}
