package com.freeme.discovery.ui.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freeme.discovery.R;
import com.freeme.discovery.base.Body;
import com.freeme.discovery.base.BodyFactory;
import com.freeme.discovery.base.DownloadInfo;
import com.freeme.discovery.base.MessageCode;
import com.freeme.discovery.bean.apps.HotApp;
import com.freeme.discovery.bean.apps.HotAppBody;
import com.freeme.discovery.ui.adapter.MyAdapter;
import com.freeme.discovery.utils.CommonUtils;
import com.freeme.discovery.utils.NetworkUtils;
import com.freeme.discovery.view.CircleMenu;
import com.freeme.discovery.view.RadarScene;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private RadarScene mHoloScene;

    private MyAdapter mMyAdapter;

    private CircleMenu mCircleMenu;
    private TextView mAppSearch;
    private TextView mVideoSearch;
    private ImageView mBack;

    private AVLoadingIndicatorView avLoadingIndicatorView;

    private ViewStub mGuideViewStub;
    private TextView mGuideStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        avLoadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.discovery_loading_layout) ;
        //avLoadingIndicatorView.show();

        mGuideViewStub = (ViewStub)findViewById(R.id.discovery_guide);
        mGuideViewStub.setVisibility(View.VISIBLE);
        mGuideStart = (TextView)findViewById(R.id.discovery_rule_start);
        mGuideStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGuideViewStub.setVisibility(View.GONE);
            }
        });

        mHoloScene = (RadarScene) findViewById(R.id.folder_radar);

        mBack = (ImageView)findViewById(R.id.discovery_radar_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCircleMenu = (CircleMenu)findViewById(R.id.discovery_circlemenu);
        mCircleMenu.setMenuBottomMargin(dip2px(this, 20.0f));
        mCircleMenu.setOperationHeight(dip2px(this, 56.0f));
        mCircleMenu.setItemMargin(dip2px(this, 45.0f));
        mCircleMenu.setMenuItemClickListener(new CircleMenu.MenuItemClickListener() {
            @Override
            public void menuItemClick(int postion) {
                refreshData();
            }
        });

        mVideoSearch = new TextView(this);
        mVideoSearch.setText(getResources().getString(R.string.video_search));
        mVideoSearch.setAllCaps(true);
        mVideoSearch.setGravity(17);
        mVideoSearch.setTextSize(26);
        CommonUtils.setTextShadow(mVideoSearch,
                getResources().getColor(R.color.discovery_radar_wave),getResources().getColor(R.color.discovery_shadow));
        RelativeLayout.LayoutParams mVidoLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
        mCircleMenu.addView(mVideoSearch, mVidoLayoutParams);

        mAppSearch = new TextView(this);
        mAppSearch.setText(getResources().getString(R.string.app_search));
        mAppSearch.setAllCaps(true);
        mAppSearch.setGravity(17);
        mAppSearch.setTextSize(26);
        CommonUtils.setTextShadow(mAppSearch,
                getResources().getColor(R.color.discovery_radar_wave),getResources().getColor(R.color.discovery_shadow));
        RelativeLayout.LayoutParams mAppLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
        mCircleMenu.addView(mAppSearch, mAppLayoutParams);

        mMyAdapter = new MyAdapter();

        mHoloScene.setAdapter(mMyAdapter);

        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Log.i(TAG, "  imsi =   " + tm.getSubscriberId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        onFirstLoadData();

    }

    protected void onResume() {
        super.onResume();
    }

    private void onFirstLoadData(){
        new GetOnlineHotAppsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, false);
    }

    public void refreshData(){
        avLoadingIndicatorView.show();
        new GetOnlineHotAppsData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, false);
    }

    public  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public class GetOnlineHotAppsData extends
            AsyncTask<Boolean, Void, DownloadInfo>{

        @Override
        protected DownloadInfo doInBackground(Boolean... params) {
            DownloadInfo result = null;
            try {
                String serverString = null;
                if (params[0]) {

                } else {
                    serverString= NetworkUtils.getAppsStringFromServer(MessageCode.MESSAGE_CODE_HOT_APPS);
                    if (!TextUtils.isEmpty(serverString)) {

                    }
                }
                Log.d(TAG, "  serverString = " + serverString);
                if(!TextUtils.isEmpty(serverString)){
                    result = splitHotAppsServerData(serverString);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DownloadInfo result) {
            super.onPostExecute(result);
            ArrayList<HotApp> hotApps = null;
            if (result != null) {
                Body body = result.getBodyInfo();
                if (body != null && body instanceof HotAppBody) {
                    HotAppBody hotAppBody = (HotAppBody) body;
                    hotApps=hotAppBody.getHotApps();

                }
            }
            if(mHoloScene != null){
                mHoloScene.updateData(hotApps);
            }
            //updateHotApps(hotApps);
            avLoadingIndicatorView.hide();
        }
    }

    public DownloadInfo splitHotAppsServerData(String result) {
        DownloadInfo mInfo = null;
        if (TextUtils.isEmpty(result)) {
            return mInfo;
        }

        String jsonString = null;
        try {
            JSONObject jsonObject = new JSONObject(result.trim());
            jsonString = jsonObject.getString("body");
        } catch (JSONException e) {
            e.printStackTrace();
            return mInfo;
        }

        JSONObject object;
        JSONArray array = null;
        try {
            object = new JSONObject(jsonString);

            array = object.getJSONArray("apks");
            mInfo = new DownloadInfo();
            HotAppBody bodyInfo = BodyFactory.createHotAppBody();
            mInfo.mBodyInfo = bodyInfo;

            ArrayList<HotApp> hotApps = bodyInfo.getHotApps();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jobject = array.getJSONObject(i);
                HotApp hotApp = new HotApp();
//                hotApp.setApkId(jobject.getString("appId"));// object.getString("")
                hotApp.setApkName(jobject.getString("apkName"));
                hotApp.setDownloadNum(jobject.getLong("downloadNum"));
                hotApp.setFileSize(jobject.getString("fileSize"));
                hotApp.setDownloadUrl(jobject.getString("downloadUrl"));
                hotApp.setIconUrl(jobject.getString("imageUrl"));
                hotApp.setPackageName(jobject.getString("packageName"));
                hotApps.add(hotApp);
            }

            bodyInfo.setErrorCode(object.getString("errorCode"));
        } catch (Exception e) {
            Log.v(TAG, "e" + e.toString());
            e.printStackTrace();
        }

        return mInfo;
    }

}
