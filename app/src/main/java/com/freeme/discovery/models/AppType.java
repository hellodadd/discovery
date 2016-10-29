package com.freeme.discovery.models;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiObject;

/**
 * Created by zwb on 2016/10/28.
 */

public class AppType extends DroiObject{

    @DroiExpose
    private String name;

    @DroiExpose
    private String mainType;

    @DroiExpose
    private int typeId;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setMainType(String mainType){
        this.mainType = mainType;
    }

    public String getMainType(){
        return this.mainType;
    }

    public void setTypeId(int typeId){
        this.typeId = typeId;
    }

    public int getTypeId(){
        return typeId;
    }


}