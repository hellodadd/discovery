package com.freeme.discovery.ui.adapter;

import android.content.Context;
import android.view.View;

import com.freeme.discovery.common.AsyncImageCache;
import com.freeme.discovery.view.ContentTempleteView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by server on 16-11-4.
 */

public abstract class BaseAdapter<E> {

    public Context mContext;

    public ArrayList<E> infoList;

    public AsyncImageCache mAsyncImageCache = null;

    public List mapList = new ArrayList();

    private final static int RADIAN_0 = 5;// 0 cicir show 5
    private final static int RADIAN_1 = 8;//
    private final static int RADIAN_2 = 13;//
    private final static int RADIAN_3 = 16;//
    private final static int RADIAN_4 = 18;//

    public BaseAdapter(Context context){
        mContext = context;
        mAsyncImageCache = AsyncImageCache.from(mContext);
    }

    public void setInfoList(ArrayList<E> list){
        infoList = list;
    }

    public ArrayList<E> getInfoList(){
        return  infoList;
    }

    public int getInfoSize(){
        return infoList.size();
    }

    public ContentTempleteView CreateInfoView(E info){
        return null;
    }

    public void initMapList(){
        int radianStep = 0;
        mapList.clear();
        for(int i = 0; i < RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 70;
        }
        radianStep = 0;
        for(int i = RADIAN_0; i < RADIAN_1 + RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 45;
        }
        radianStep = 0;
        for(int i = RADIAN_1 + RADIAN_0; i < RADIAN_2 + RADIAN_1 + RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 27;
        }
        radianStep = 0;
        for(int i = RADIAN_2 + RADIAN_1 + RADIAN_0; i < RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 360/RADIAN_3;
        }
        radianStep = 0;
        for(int i = RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0; i < RADIAN_4 + RADIAN_3 + RADIAN_2 + RADIAN_1 + RADIAN_0; i++) {
            mapList.add(radianStep);
            radianStep += 360/RADIAN_4;
        }
    }

    public int getRadian(){
        return 0;
    }
    public int getDistance(){
        return 0;
    }
    public int getRadius(){
        return 0;
    }

    public void showAttentionAnim(final ContentTempleteView view){

    }


}
