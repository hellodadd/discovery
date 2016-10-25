package com.freeme.discovery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freeme.discovery.R;
import com.freeme.discovery.base.Body;
import com.freeme.discovery.base.BodyFactory;
import com.freeme.discovery.base.DownloadInfo;
import com.freeme.discovery.base.MessageCode;
import com.freeme.discovery.bean.AppBean;
import com.freeme.discovery.bean.CategoryBean;
import com.freeme.discovery.bean.apps.HotApp;
import com.freeme.discovery.bean.apps.HotAppBody;
import com.freeme.discovery.http.HttpInterface;
import com.freeme.discovery.http.RequstApkListClient;
import com.freeme.discovery.http.RequstCategoryClient;
import com.freeme.discovery.ui.adapter.MyAdapter;
import com.freeme.discovery.utils.CommonUtils;
import com.freeme.discovery.utils.NetworkUtils;
import com.freeme.discovery.view.CircleMenu;
import com.freeme.discovery.view.CircleMenuItemView;
import com.freeme.discovery.view.RadarScene;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "MainActivity";

    private final static String KEY_FIRST = "firststart";

    private RadarScene mHoloScene;

    private MyAdapter mMyAdapter;

    private CircleMenu mCircleMenu;
    private int mCircleMenuNum;
    private TextView mAppSearch;
    private TextView mVideoSearch;

    private RequstCategoryClient requstCategoryClient;
    private List<CategoryBean.CategorysBean> categorys;

    private RequstApkListClient requstApkListClient;

    private int mCurentCategoryId;


    private ImageView mBack;

    private AVLoadingIndicatorView avLoadingIndicatorView;

    private ViewStub mGuideViewStub;
    private TextView mGuideStart;


    private ImageView mShareButton;
    private ImageView mRefreshButton;
    private ImageView mScanStopButton;
    private ImageView mFavriteButton;
    private boolean mBooleanRadarScan;

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
    public static final Executor fixedThreadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory,new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        avLoadingIndicatorView = (AVLoadingIndicatorView)findViewById(R.id.discovery_loading_layout) ;
        avLoadingIndicatorView.show();

        if(!CommonUtils.isNetworkConnected(this)){
            showNetConnectError();
        }

        SharedPreferences sp = this.getSharedPreferences("discovery", MODE_PRIVATE);
        boolean firststart = sp.getBoolean(KEY_FIRST, true);

        mGuideViewStub = (ViewStub)findViewById(R.id.discovery_guide);
        mGuideViewStub.inflate();
        mGuideStart = (TextView)findViewById(R.id.discovery_rule_start);
        if(mGuideStart != null){
            mGuideStart.setOnClickListener(this);
        }
        if(firststart) {
            mGuideViewStub.setVisibility(View.VISIBLE);
        }else{
            mGuideViewStub.setVisibility(View.GONE);
        }

        mShareButton = (ImageView)findViewById(R.id.discovery_share_button);
        mShareButton.setOnClickListener(this);
        mRefreshButton = (ImageView)findViewById(R.id.discovery_refresh_button);
        mRefreshButton.setOnClickListener(this);
        mScanStopButton = (ImageView)findViewById(R.id.discovery_rotate_button);
        mScanStopButton.setOnClickListener(this);
        mFavriteButton = (ImageView)findViewById(R.id.discovery_type_button);
        mFavriteButton.setOnClickListener(this);

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
            public void menuItemClick(int categoryId) {
                mCurentCategoryId = categoryId;
                refreshData(categoryId);
            }
        });

        mMyAdapter = new MyAdapter();

        mHoloScene.setAdapter(mMyAdapter);

        requstCategory();

        //onFirstLoadData();

       // createCircleMenu();

    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause(){
        super.onPause();

        if(mHoloScene != null){
            mHoloScene.stopItemAttentionAni();
        }
    }


    public void refreshData(){
        avLoadingIndicatorView.show();
        mHoloScene.clearData();
        new GetOnlineHotAppsData().executeOnExecutor(fixedThreadPool, false);
    }

    public void createCircleMenu(){
           mCircleMenuNum = 5;
           for(int i = 0; i < mCircleMenuNum; i++){
               TextView view = new TextView(this);
               view.setText(i + " menu ");
               view.setAllCaps(true);
               view.setGravity(17);
               view.setTextSize(18);
               CommonUtils.setTextShadow(view,
                       getResources().getColor(R.color.discovery_radar_wave),getResources().getColor(R.color.discovery_shadow));
               RelativeLayout.LayoutParams mVidoLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
               mCircleMenu.addView(view, mVidoLayoutParams);
           }
    }

    public void createCircleMenu(List<CategoryBean.CategorysBean> categorys){
        mCircleMenu.removeAllViews();

                CircleMenuItemView itemView = new CircleMenuItemView(this);
                itemView.setText("游戏");
                itemView.setAllCaps(true);
                itemView.setGravity(17);
                itemView.setTextSize(18);
                itemView.setCategoryPid(173);
                CommonUtils.setTextShadow(itemView,
                        getResources().getColor(R.color.discovery_radar_wave),getResources().getColor(R.color.discovery_shadow));
                RelativeLayout.LayoutParams mVidoLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
                mCircleMenu.addView(itemView, mVidoLayoutParams);


            CircleMenuItemView itemView1 = new CircleMenuItemView(this);
            itemView1.setText("购物");
            itemView1.setAllCaps(true);
            itemView1.setGravity(17);
            itemView1.setTextSize(18);
            itemView1.setCategoryPid(118);
            CommonUtils.setTextShadow(itemView1,
                    getResources().getColor(R.color.discovery_radar_wave),getResources().getColor(R.color.discovery_shadow));
            RelativeLayout.LayoutParams mitemView1Params = new RelativeLayout.LayoutParams(-2, -2);
            mCircleMenu.addView(itemView1, mitemView1Params);

            CircleMenuItemView itemView2 = new CircleMenuItemView(this);
            itemView2.setText("交友");
            itemView2.setAllCaps(true);
            itemView2.setGravity(17);
            itemView2.setTextSize(18);
            itemView2.setCategoryPid(46);
            CommonUtils.setTextShadow(itemView2,
                    getResources().getColor(R.color.discovery_radar_wave),getResources().getColor(R.color.discovery_shadow));
            RelativeLayout.LayoutParams mitemView2Params = new RelativeLayout.LayoutParams(-2, -2);
            mCircleMenu.addView(itemView2, mitemView2Params);

        onFirstLoadData();

    }

    private void onFirstLoadData(){
        requstApkInfo(1, 40, 118);
    }

    public void refreshData(int categoryId){
        avLoadingIndicatorView.show();
        mHoloScene.clearData();
        requstApkInfo(1, 40, categoryId);
    }

    public  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.software_share_text));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.software_share_titile)));
    }

    private void stopRadarSacn(){
        if(!mBooleanRadarScan){
            if(mHoloScene != null){
                mHoloScene.stopRadarScan();
            }
        }else{
            if(mHoloScene != null){
                mHoloScene.startRadarScan();
            }
        }
        mBooleanRadarScan = !mBooleanRadarScan;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discovery_share_button:
                shareApp();
                break;
            case R.id.discovery_refresh_button:
                refreshData(118);
                break;
            case R.id.discovery_rotate_button:
                stopRadarSacn();
                break;
            case R.id.discovery_type_button:
                break;
            case R.id.discovery_rule_start:
                if(mGuideViewStub != null) {
                    mGuideViewStub.setVisibility(View.GONE);
                }
                SharedPreferences sp = MainActivity.this.getSharedPreferences("discovery", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(KEY_FIRST, false);
                editor.commit();
                break;
        }
    }

    private void showNetConnectError(){
        LayoutInflater inflater = getLayoutInflater();

        View dialog = inflater.inflate(R.layout.discovery_connect_error_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CoustomDialog);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();

        TextView cancleButton = (TextView)dialog.findViewById(R.id.connect_setting_cancel);
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                finish();
            }
        });

        Button setButton = (Button)dialog.findViewById(R.id.connect_setting_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.settings.SETTINGS"));
                finish();
            }
        });


        alertDialog.show();
    }

    public void requstCategory(){
        if(requstCategoryClient == null){
            requstCategoryClient = RequstCategoryClient.getInstance();
        }

        requstCategoryClient.getCategory(HttpInterface.DISCOBERY_TOKEN,
                HttpInterface.DISCOBERY_KEY, new RequstCategoryClient.onRequstCategoryListen() {
                    @Override
                    public void onRequstCategorySuccess(Call<CategoryBean> call, Response<CategoryBean> response) {
                        if(response != null){
                            categorys = response.body().getCategorys();

                            createCircleMenu(categorys);
                        }
                    }

                    @Override
                    public void onRequstCategoryFailure(Call<CategoryBean> call, Throwable t) {

                    }
                });
    }

    private void requstApkInfo(int page, int size, int categoryId){
        if(requstApkListClient == null){
            requstApkListClient = RequstApkListClient.getInstance();
        }

        Log.i(TAG, "reggg --------- response id " + categoryId);

        requstApkListClient.getApkInfo(HttpInterface.DISCOBERY_TOKEN,
                HttpInterface.DISCOBERY_KEY, page, size, categoryId, new RequstApkListClient.onRequstApkListen() {
                    @Override
                    public void onRequstApkSuccess(Call<AppBean> call, Response<AppBean> response) {
                        Log.i(TAG, "reggg --------- response " + response.body().getApkInfos().size());
                        if(response != null){
                            if(mHoloScene != null){
                                mHoloScene.updateData(response.body());
                            }
                            //updateHotApps(hotApps);
                            avLoadingIndicatorView.hide();
                        }
                    }

                    @Override
                    public void onRequstApkFailure(Call<AppBean> call, Throwable t) {

                    }
                });
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
