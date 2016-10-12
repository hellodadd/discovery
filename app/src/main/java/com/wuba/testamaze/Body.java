package com.wuba.testamaze;

/**
 * Created by server on 16-10-10.
 */

public class Body {
    private String ver;
    private String errorCode;
    private int total;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getVersion() {
        return ver;
    }

    public void setVersion(String version) {
        this.ver = version;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
