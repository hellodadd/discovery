package com.freeme.discovery.utils;

import android.util.Log;

import com.droi.sdk.DroiCallback;
import com.droi.sdk.DroiError;
import com.droi.sdk.core.DroiPermission;
import com.droi.sdk.core.DroiUser;
import com.freeme.discovery.bean.AppBean;
import com.freeme.discovery.http.HttpInterface;
import com.freeme.discovery.http.RequstApkListClient;
import com.freeme.discovery.http.RequstCategoryApi;
import com.freeme.discovery.http.TestShopApi;
import com.freeme.discovery.http.TestShopService;
import com.freeme.discovery.http.TestVideoApi;
import com.freeme.discovery.http.TestVideoSerivce;
import com.freeme.discovery.models.AppInfo;
import com.freeme.discovery.models.AppType;
import com.freeme.discovery.models.ShopInfo;
import com.freeme.discovery.models.VideoInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zwb on 2016/10/28.
 */

public class TestDroiBaas {
    private final static String TAG = "TestDroiBaas";

    public static void CreateAppType(){
        DroiUser currentUser = DroiUser.getCurrentUser();

        // 设置权限为所有用户只读，拥有者可读可写
        DroiPermission permission = new DroiPermission();
        permission.setPublicReadPermission(true);
        permission.setUserReadPermission(currentUser.getObjectId(), true);
        permission.setUserWritePermission(currentUser.getObjectId(), true);

        AppType appType = new AppType();
        appType.setName("sns");
        appType.setMainType("video");
        appType.setTypeId(118);

        appType.setPermission(permission);

        appType.saveInBackground(new DroiCallback<Boolean>() {
            @Override
            public void result(Boolean aBoolean, DroiError droiError) {
                Log.i(TAG, " ------ CreateAppType ------ " + droiError.isOk());
            }
        });
    }

    public static void requstApkInfo(int page, int size, int categoryId) {

        RequstApkListClient requstApkListClient = RequstApkListClient.getInstance();

        requstApkListClient.getApkInfo(HttpInterface.DISCOBERY_TOKEN,
                HttpInterface.DISCOBERY_KEY, page, size, categoryId, new RequstApkListClient.onRequstApkListen() {
                    @Override
                    public void onRequstApkSuccess(Call<AppBean> call, Response<AppBean> response) {
                        Log.i(TAG, "reggg --------- response " + response.body().getApkInfos().size());
                        if (response != null) {
                                List<AppBean.ApkInfosBean> infos = response.body().getApkInfos();
                                for(AppBean.ApkInfosBean infosBean : infos){

                                    DroiUser currentUser = DroiUser.getCurrentUser();

                                    // 设置权限为所有用户只读，拥有者可读可写
                                    DroiPermission permission = new DroiPermission();
                                    permission.setPublicReadPermission(true);
                                    permission.setUserReadPermission(currentUser.getObjectId(), true);
                                    permission.setUserWritePermission(currentUser.getObjectId(), true);

                                    AppInfo appInfo = new AppInfo();
                                    appInfo.setMainType("game"); //game : 173; sns 46
                                    appInfo.setSname(infosBean.getSname());
                                    appInfo.setCateid(infosBean.getCateid());
                                    appInfo.setCatename(infosBean.getCatename());
                                    appInfo.setBrief(infosBean.getBrief());
                                    appInfo.setDescription(infosBean.getDescription());
                                    appInfo.setDocid(infosBean.getDocid());
                                    appInfo.setPlatform(infosBean.getPlatform());
                                    appInfo.setVersion(infosBean.getVersion());
                                    appInfo.setUrl("http://m.zhuoyi.com/detail.php?apk_id="+infosBean.getDocid());
                                    appInfo.setReleasedate(infosBean.getReleasedate());
                                    appInfo.setFilesize(infosBean.getFilesize());
                                    appInfo.setFilename(infosBean.getFilename());
                                    appInfo.setIconurl(infosBean.getIconurl());
                                    appInfo.setApkimgurls(infosBean.getApkimgurls());
                                    appInfo.setVersioncode(infosBean.getVersioncode());
                                    appInfo.setPackagename(infosBean.getPackagename());
                                    appInfo.setCharge(infosBean.getCharge());
                                    appInfo.setDownloadnum(infosBean.getDownloadnum());
                                    appInfo.setFilemd5(infosBean.getFilemd5());
                                    appInfo.setCompany_type(infosBean.getCompany_type());
                                    appInfo.setHot(infosBean.getHot());

                                    appInfo.setPermission(permission);

                                    appInfo.saveInBackground(new DroiCallback<Boolean>() {
                                        @Override
                                        public void result(Boolean aBoolean, DroiError droiError) {
                                            Log.i(TAG, "  aBoolean  " + droiError.isOk());
                                        }
                                    });
                                }
                        }
                    }

                    @Override
                    public void onRequstApkFailure(Call<AppBean> call, Throwable t) {

                    }
                });
    }

    public static void requstTestVieo() {
        DroiUser currentUser = DroiUser.getCurrentUser();
        // 设置权限为所有用户只读，拥有者可读可写
        DroiPermission permission = new DroiPermission();
        permission.setPublicReadPermission(true);
        permission.setUserReadPermission(currentUser.getObjectId(), true);
        permission.setUserWritePermission(currentUser.getObjectId(), true);

        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setMainType("video");
        videoInfo.setSname("偶滴神啊");
        videoInfo.setIconurl("http://i2.itc.cn/20121211/ab1_9c88f856_7449_c672_0275_cfb0c8b6ddbf_1.jpg");
        videoInfo.setUrl("http://my.tv.sohu.com/u/vw/50179255");

        videoInfo.setPermission(permission);


        videoInfo.saveInBackground(new DroiCallback<Boolean>() {
            @Override
            public void result(Boolean aBoolean, DroiError droiError) {
                Log.i(TAG, " ------ CreateAppType ------ " + droiError.getCode());
            }
        });
    }

    public static void requstTestShop() {
        OkHttpClient.Builder okhttp = new OkHttpClient().newBuilder();
        okhttp.connectTimeout(5, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(okhttp.build())
                .baseUrl("http://api.tianapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TestShopService testShopService = retrofit.create(TestShopService.class);

        Call<TestShopApi> getShopInfo = testShopService.getShopInfo("ca9d7457935cebee182d5dca9441ec79", 30);

        getShopInfo.enqueue(new Callback<TestShopApi>() {
            @Override
            public void onResponse(Call<TestShopApi> call, Response<TestShopApi> response) {
                Log.i("zwb", " ------ response = " + response.body().getNewslist().size());
                if(response != null){
                    List<TestShopApi.NewslistBean> newslistBeen = response.body().getNewslist();
                    if(newslistBeen != null && newslistBeen.size() > 0){
                        for(TestShopApi.NewslistBean news : newslistBeen) {
                            DroiUser currentUser = DroiUser.getCurrentUser();
                            // 设置权限为所有用户只读，拥有者可读可写
                            DroiPermission permission = new DroiPermission();
                            permission.setPublicReadPermission(true);
                            permission.setUserReadPermission(currentUser.getObjectId(), true);
                            permission.setUserWritePermission(currentUser.getObjectId(), true);

                            ShopInfo shopInfo = new ShopInfo();
                            shopInfo.setMainType("shop");
                            shopInfo.setPrice((float) (10 + Math.random() * 50));
                            shopInfo.setSname(news.getTitle());
                            shopInfo.setUrl(news.getUrl());
                            shopInfo.setIconurl(news.getPicUrl());

                            shopInfo.setPermission(permission);

                            shopInfo.saveInBackground(new DroiCallback<Boolean>() {
                                @Override
                                public void result(Boolean aBoolean, DroiError droiError) {
                                    Log.i("zwb", " ------ droiError = " + droiError.isOk());
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TestShopApi> call, Throwable t) {

            }
        });
    }
}
