package com.freeme.discovery;

import android.app.Application;

import com.droi.sdk.core.Core;
import com.droi.sdk.core.DroiObject;
import com.freeme.discovery.models.AppInfo;
import com.freeme.discovery.models.AppType;

/**
 * Created by zwb on 2016/10/28.
 */

public class DiscoveryApplication extends Application{

    public void onCreate(){
        super.onCreate();

        Core.initialize(this);

        DroiObject.registerCustomClass(AppType.class);
        DroiObject.registerCustomClass(AppInfo.class);
    }
}
