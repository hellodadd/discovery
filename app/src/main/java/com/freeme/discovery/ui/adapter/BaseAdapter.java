package com.freeme.discovery.ui.adapter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by server on 16-11-4.
 */

public abstract class BaseAdapter<E> {

    public Context mContext;

    public ArrayList<E> infoList;

    public BaseAdapter(Context context){
        mContext = context;
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

    public View CreateInfoView(ArrayList<E> list){
        return null;
    }


}
