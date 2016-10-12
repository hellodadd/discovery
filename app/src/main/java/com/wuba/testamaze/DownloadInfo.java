package com.wuba.testamaze;

/**
 * Created by server on 16-10-10.
 */

public class DownloadInfo {
    public Header mHeaderInfo;
    public Body mBodyInfo;

    public Header getHeaderInfo() {
        return mHeaderInfo;
    }

    public void setHeaderInfo(Header mHeaderInfo) {
        this.mHeaderInfo = mHeaderInfo;
    }

    public Body getBodyInfo() {
        return mBodyInfo;
    }

    public void setBodyInfo(Body mBodyInfo) {
        this.mBodyInfo = mBodyInfo;
    }

    public DownloadInfo() {
        mHeaderInfo = new Header();
    }
}
