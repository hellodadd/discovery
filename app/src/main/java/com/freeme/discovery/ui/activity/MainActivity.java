package com.freeme.discovery.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droi.sdk.DroiError;
import com.droi.sdk.core.DroiCondition;
import com.droi.sdk.core.DroiQuery;
import com.droi.sdk.core.DroiQueryCallback;
import com.freeme.discovery.R;
import com.freeme.discovery.bean.CategoryBean;
import com.freeme.discovery.http.RequstApkListClient;
import com.freeme.discovery.http.RequstCategoryClient;
import com.freeme.discovery.models.AppInfo;
import com.freeme.discovery.models.AppType;
import com.freeme.discovery.models.ShopInfo;
import com.freeme.discovery.models.VideoInfo;
import com.freeme.discovery.ui.adapter.MyAdapter;
import com.freeme.discovery.utils.CommonUtils;
import com.freeme.discovery.utils.TestDroiBaas;
import com.freeme.discovery.view.CircleMenu;
import com.freeme.discovery.view.CircleMenuItemView;
import com.freeme.discovery.view.RadarScene;
import com.wang.avi.AVLoadingIndicatorView;

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


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        RadarScene.onItemClickListener {
    private final static String TAG = "MainActivity";

    private final static String KEY_FIRST = "firststart";

    private RadarScene radarScene;

    private CircleMenu mCircleMenu;

    ArrayList<AppType> appTypeList;

    private String mCurentMainType;


    private ImageView mBack;

    private AVLoadingIndicatorView avLoadingIndicatorView;

    private ViewStub mGuideViewStub;
    private TextView mGuideStart;


    private ImageView mShareButton;
    private ImageView mRefreshButton;
    private ImageView mScanStopButton;
    private ImageView mFavriteButton;
    private boolean mBooleanRadarScan;

    private boolean isRefreshing;

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
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.discovery_loading_layout);
        avLoadingIndicatorView.show();

        if (!CommonUtils.isNetworkConnected(this)) {
            showNetConnectError();
        }

        SharedPreferences sp = this.getSharedPreferences("discovery", MODE_PRIVATE);
        boolean firststart = sp.getBoolean(KEY_FIRST, true);

        mGuideViewStub = (ViewStub) findViewById(R.id.discovery_guide);
        mGuideViewStub.inflate();
        mGuideStart = (TextView) findViewById(R.id.discovery_rule_start);
        if (mGuideStart != null) {
            mGuideStart.setOnClickListener(this);
        }
        if (firststart) {
            mGuideViewStub.setVisibility(View.VISIBLE);
        } else {
            mGuideViewStub.setVisibility(View.GONE);
        }

        mShareButton = (ImageView) findViewById(R.id.discovery_share_button);
        mShareButton.setOnClickListener(this);
        mRefreshButton = (ImageView) findViewById(R.id.discovery_refresh_button);
        mRefreshButton.setOnClickListener(this);
        mScanStopButton = (ImageView) findViewById(R.id.discovery_rotate_button);
        mScanStopButton.setOnClickListener(this);

        radarScene = (RadarScene) findViewById(R.id.folder_radar);
        radarScene.setOnItemClickListener(this);

        mBack = (ImageView) findViewById(R.id.discovery_radar_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCircleMenu = (CircleMenu) findViewById(R.id.discovery_circlemenu);
        mCircleMenu.setMenuBottomMargin(dip2px(this, 20.0f));
        mCircleMenu.setOperationHeight(dip2px(this, 56.0f));
        mCircleMenu.setItemMargin(dip2px(this, 45.0f));
        mCircleMenu.setMenuItemClickListener(new CircleMenu.MenuItemClickListener() {
            @Override
            public void menuItemClick(String mainType) {
                mCurentMainType = mainType;
                refreshData(mainType);
            }
        });

        initAppType();

        //TestDroiBaas.CreateAppType();
        //TestDroiBaas.requstApkInfo(1,40, 173);

        //TestDroiBaas.requstTestVieo();

        //TestDroiBaas.requstTestShop();

    }

    protected void onResume() {
        super.onResume();

        if(radarScene != null){
            radarScene.onResume();
        }
    }

    protected void onPause() {
        super.onPause();

        if (radarScene != null) {
            radarScene.stopItemAttentionAni();
        }
    }

    private void initAppType(){
        if(appTypeList == null){
            appTypeList = new ArrayList<>();
        }

        if(appTypeList.isEmpty()){
            fetchAppTypeData();
        }
    }

    private void fetchAppTypeData(){
        DroiQuery droiQuery = DroiQuery.Builder.newBuilder()
                .query(AppType.class)
                .build();
        droiQuery.runQueryInBackground(new DroiQueryCallback<AppType>() {
            @Override
            public void result(List<AppType> list, DroiError droiError) {
                Log.i(TAG, " fetchAppTypeData getcode = " + droiError.getCode());
                //avLoadingIndicatorView.hide();
                if(droiError.isOk()){
                    if(list != null && list.size() > 0){
                        Log.i(TAG, " list === " + list.toString());
                        appTypeList.clear();
                        appTypeList.addAll(list);
                        Log.i(TAG, " appTypeList === " + appTypeList.toString());
                        createCircleMenu();
                    }
                }else{
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                    showNetConnectRetry();
                }
            }
        });
    }


    private void createCircleMenu() {
        Log.i(TAG, " createCircleMenu  " + appTypeList.toString());
        if(appTypeList != null && appTypeList.size() > 0){
            for(AppType appType : appTypeList){
                CircleMenuItemView menuItemView = new CircleMenuItemView(this);
                menuItemView.setText(appType.getName());
                menuItemView.setMainType(appType.getMainType());

                menuItemView.setAllCaps(true);
                menuItemView.setGravity(17);
                menuItemView.setTextSize(18);

                CommonUtils.setTextShadow(menuItemView,
                        getResources().getColor(R.color.discovery_radar_wave),
                        getResources().getColor(R.color.discovery_shadow));
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
                mCircleMenu.addView(menuItemView, lp);
                mCircleMenu.setmFocusIndex(CommonUtils.DEFAULT_MENU);
            }

            fetchAppInfo(appTypeList.get(CommonUtils.DEFAULT_MENU).getMainType());
        }
    }

    private void fetchAppInfo(String mainType){
        mCurentMainType = mainType;
        if(mainType.equals(CommonUtils.VIDEO_TYPE)){
            fetchVideoInfo(mainType);
            return;
        }else if(mainType.equals(CommonUtils.SHOP_TYPE)){
            fetchShopInfo(mainType);
            return;
        }
        DroiCondition condition = DroiCondition.cond("mainType",
                DroiCondition.Type.EQ, mainType);
        DroiQuery droiQuery = DroiQuery.Builder.newBuilder()
                .limit(40)
                .query(AppInfo.class)
                .where(condition)
                .build();
        droiQuery.runQueryInBackground(new DroiQueryCallback<AppInfo>() {
            @Override
            public void result(List<AppInfo> list, DroiError droiError) {
                avLoadingIndicatorView.hide();
                if(droiError.isOk()){
                    if(list != null && list.size() > 0){
                        if(radarScene != null){
                            radarScene.updateData((ArrayList<AppInfo>) list);
                        }
                    }
                }else{
                        showNetConnectRetry();
                }
                isRefreshing = false;
            }
        });
    }

    private void fetchVideoInfo(String mainType){
        DroiCondition condition = DroiCondition.cond("mainType",
                DroiCondition.Type.EQ, mainType);
        DroiQuery droiQuery = DroiQuery.Builder.newBuilder()
                .limit(CommonUtils.VIDEO_LIMI)
                .query(VideoInfo.class)
                .where(condition)
                .build();
        droiQuery.runQueryInBackground(new DroiQueryCallback<VideoInfo>() {
            @Override
            public void result(List<VideoInfo> list, DroiError droiError) {
                avLoadingIndicatorView.hide();
                if(droiError.isOk()){
                    if(list != null && list.size() > 0){
                        if(radarScene != null){
                            radarScene.updateVideoData((ArrayList<VideoInfo>) list);
                        }
                    }
                }else{
                    showNetConnectRetry();
                }
                isRefreshing = false;
            }
        });
    }

    private void fetchShopInfo(String mainType){
        DroiCondition condition = DroiCondition.cond("mainType",
                DroiCondition.Type.EQ, mainType);
        DroiQuery droiQuery = DroiQuery.Builder.newBuilder()
                .limit(CommonUtils.SHOP_LIMI)
                .query(ShopInfo.class)
                .where(condition)
                .build();
        droiQuery.runQueryInBackground(new DroiQueryCallback<ShopInfo>() {
            @Override
            public void result(List<ShopInfo> list, DroiError droiError) {
                avLoadingIndicatorView.hide();
                if(droiError.isOk()){
                    if(list != null && list.size() > 0){
                        if(radarScene != null){
                            radarScene.updateShopData((ArrayList<ShopInfo>) list);
                        }
                    }
                }else{
                    showNetConnectRetry();
                }
                isRefreshing = false;
            }
        });
    }

    public void refreshData(String mainType) {
        //Log.i(TAG, "refres ------ ");
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        radarScene.clearData();
        if(!isRefreshing) {
            isRefreshing = true;
            fetchAppInfo(mainType);
        }
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.software_share_text));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.software_share_titile)));
    }

    private void stopRadarSacn() {
        if (!mBooleanRadarScan) {
            if (radarScene != null) {
                radarScene.stopRadarScan();
            }
        } else {
            if (radarScene != null) {
                radarScene.startRadarScan();
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
                refreshData(mCurentMainType);
                break;
            case R.id.discovery_rotate_button:
                stopRadarSacn();
                break;
            case R.id.discovery_rule_start:
                if (mGuideViewStub != null) {
                    mGuideViewStub.setVisibility(View.GONE);
                }
                SharedPreferences sp = MainActivity.this.getSharedPreferences("discovery", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(KEY_FIRST, false);
                editor.commit();
                break;
        }
    }

    private void showNetConnectError() {
        /*//
        LayoutInflater inflater = getLayoutInflater();

        View dialog = inflater.inflate(R.layout.discovery_connect_error_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CoustomDialog);
        builder.setView(dialog);
        final AlertDialog alertDialog = builder.create();

        TextView cancleButton = (TextView) dialog.findViewById(R.id.connect_setting_cancel);
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                finish();
            }
        });

        Button setButton = (Button) dialog.findViewById(R.id.connect_setting_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.settings.SETTINGS"));
                finish();
            }
        });

        alertDialog.show();
        //*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//, R.style.CoustomDialog);
        builder.setTitle(getResources().getString(R.string.discovery_dialog_type_connection_error_title));
        builder.setMessage(getResources().getString(R.string.discovery_dialog_type_connection_error_content));
        builder.setNegativeButton(getResources().
                getString(R.string.discovery_dialog_type_connection_error_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
                finish();
            }
        });
        builder.setPositiveButton(getResources().getText(R.string.discovery_dialog_type_connection_error_btn_settings),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent("android.settings.SETTINGS"));
                        finish();
                    }
                });
        builder.show();
    }

    private void showNetConnectRetry(){
        LayoutInflater inflater = getLayoutInflater();
        //View view = inflater.inflate(R.layout.discovery_retry, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//, R.style.CoustomDialog);
        builder.setTitle(getResources().getString(R.string.discovery_dialog_type_connection_error_title));
        builder.setMessage(getResources().getString(R.string.discovery_dialog_type_connection_error_msg));
        builder.setNegativeButton(getResources().
                getString(R.string.discovery_dialog_type_connection_error_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
                finish();
            }
        });
        builder.setPositiveButton(getResources().getText(R.string.discovery_dialog_type_connection_retry),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(appTypeList.isEmpty()){
                            fetchAppTypeData();
                        }else{
                            refreshData(mCurentMainType);
                        }
                    }
                });
        builder.show();


        /*builder.setView(view);

        final AlertDialog dialog = builder.create();

        TextView cancleButton = (TextView) view.findViewById(R.id.connect_cancel);
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                finish();
            }
        });

        Button retryBtn = (Button) view.findViewById(R.id.connect_retry_button);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appTypeList.isEmpty()){
                    fetchAppTypeData();
                }else{
                    refreshData(mCurentMainType);
                }
                dialog.cancel();
            }
        });
        dialog.show();
        */
    }

    @Override
    public void onItemClick(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
