package com.freeme.discovery.models;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiObject;

/**
 * Created by server on 16-11-2.
 */

public class ShopInfo extends DroiObject{
    @DroiExpose
    private String mainType;

    @DroiExpose
    private String sname;

    @DroiExpose
    private String iconurl;

    @DroiExpose
    private String url;

    @DroiExpose
    private float price;

    public void setMainType(String mainType){
        this.mainType = mainType;
    }

    public String getMainType(){
        return mainType;
    }

    public void setSname(String sname){
        this.sname = sname;
    }

    public String getSname(){
        return  sname;
    }

    public void setIconurl(String iconurl){
        this.iconurl = iconurl;
    }

    public String getIconurl(){
        return iconurl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public float getPrice(){
        return price;
    }
}
