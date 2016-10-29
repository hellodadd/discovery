package com.freeme.discovery.models;

import com.droi.sdk.core.DroiExpose;
import com.droi.sdk.core.DroiObject;

/**
 * Created by zwb on 2016/10/28.
 */

public class AppInfo extends DroiObject {

    @DroiExpose
    private String sname;
    @DroiExpose
    private String catename;
    @DroiExpose
    private String cateid;
    @DroiExpose
    private String brief;
    @DroiExpose
    private String description;
    @DroiExpose
    private int docid;
    @DroiExpose
    private String platform;
    @DroiExpose
    private String version;
    @DroiExpose
    private String url;
    @DroiExpose
    private String releasedate;
    @DroiExpose
    private String filesize;
    @DroiExpose
    private String filename;
    @DroiExpose
    private String iconurl;
    @DroiExpose
    private String apkimgurls;
    @DroiExpose
    private String versioncode;
    @DroiExpose
    private String packagename;
    @DroiExpose
    private int charge;
    @DroiExpose
    private int downloadnum;
    @DroiExpose
    private String filemd5;
    @DroiExpose
    private int company_type;
    @DroiExpose
    private int hot;
    @DroiExpose
    private String mainType;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public String getCateid() {
        return cateid;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDocid() {
        return docid;
    }

    public void setDocid(int docid) {
        this.docid = docid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getApkimgurls() {
        return apkimgurls;
    }

    public void setApkimgurls(String apkimgurls) {
        this.apkimgurls = apkimgurls;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getDownloadnum() {
        return downloadnum;
    }

    public void setDownloadnum(int downloadnum) {
        this.downloadnum = downloadnum;
    }

    public String getFilemd5() {
        return filemd5;
    }

    public void setFilemd5(String filemd5) {
        this.filemd5 = filemd5;
    }

    public int getCompany_type() {
        return company_type;
    }

    public void setCompany_type(int company_type) {
        this.company_type = company_type;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getMainType(){
        return mainType;
    }

    public void setMainType(String mainType){
        this.mainType = mainType;
    }
}
