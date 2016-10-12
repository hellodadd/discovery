package com.wuba.testamaze;

import java.util.ArrayList;

/**
 * Created by server on 16-10-11.
 */

public class HotAppBody extends Body{
    private ArrayList<HotApp> hotApps=new ArrayList<HotApp>();

    public ArrayList<HotApp> getHotApps() {
        return hotApps;
    }

    public void setHotApps(ArrayList<HotApp> hotApps) {
        this.hotApps = hotApps;
    }
}
