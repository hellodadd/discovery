package com.freeme.discovery.bean.apps;

import com.freeme.discovery.base.Body;

import java.util.ArrayList;


public class HotAppBody extends Body {
    private ArrayList<HotApp> hotApps=new ArrayList<HotApp>();

    public ArrayList<HotApp> getHotApps() {
        return hotApps;
    }

    public void setHotApps(ArrayList<HotApp> hotApps) {
        this.hotApps = hotApps;
    }
}
