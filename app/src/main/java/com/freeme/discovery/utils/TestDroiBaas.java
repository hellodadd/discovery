package com.freeme.discovery.utils;

import android.util.Log;

import com.droi.sdk.DroiCallback;
import com.droi.sdk.DroiError;
import com.freeme.discovery.bean.AppBean;
import com.freeme.discovery.http.HttpInterface;
import com.freeme.discovery.http.RequstApkListClient;
import com.freeme.discovery.models.AppInfo;
import com.freeme.discovery.models.AppType;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by zwb on 2016/10/28.
 */

public class TestDroiBaas {
    private final static String TAG = "TestDroiBaas";

    public static void CreateAppType(){
        AppType appType = new AppType();
        appType.setName("game");
        appType.setMainType("game");
        appType.setTypeId(46);

        appType.saveInBackground(new DroiCallback<Boolean>() {
            @Override
            public void result(Boolean aBoolean, DroiError droiError) {
                Log.i(TAG, " ------ CreateAppType ------ " + droiError.isOk());
            }
        });
    }

    public void requstApkInfo(int page, int size, int categoryId) {

        RequstApkListClient requstApkListClient = RequstApkListClient.getInstance();

        requstApkListClient.getApkInfo(HttpInterface.DISCOBERY_TOKEN,
                HttpInterface.DISCOBERY_KEY, page, size, categoryId, new RequstApkListClient.onRequstApkListen() {
                    @Override
                    public void onRequstApkSuccess(Call<AppBean> call, Response<AppBean> response) {
                        Log.i(TAG, "reggg --------- response " + response.body().getApkInfos().size());
                        if (response != null) {
                                List<AppBean.ApkInfosBean> infos = response.body().getApkInfos();
                                for(AppBean.ApkInfosBean infosBean : infos){
                                    AppInfo appInfo = new AppInfo();
                                    appInfo.setMainType("sns");
                                    appInfo.setSname(infosBean.getSname());
                                    appInfo.setCateid(infosBean.getCateid());
                                    appInfo.setCatename(infosBean.getCatename());
                                    appInfo.setBrief(infosBean.getBrief());
                                    appInfo.setDescription(infosBean.getDescription());
                                    appInfo.setDocid(infosBean.getDocid());
                                    appInfo.setPlatform(infosBean.getPlatform());
                                    appInfo.setVersion(infosBean.getVersion());
                                    appInfo.setUrl(infosBean.getUrl());
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
}
