package com.freeme.discovery.bean;

import java.util.List;

/**
 * Created by server on 16-10-25.
 */

public class CategoryBean {
    /**
     * categoryname : 应用分类
     * categoryid : 34
     * categorypid : 0
     */

    private List<CategorysBean> categorys;

    public List<CategorysBean> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<CategorysBean> categorys) {
        this.categorys = categorys;
    }

    public static class CategorysBean {
        private String categoryname;
        private int categoryid;
        private int categorypid;

        public String getCategoryname() {
            return categoryname;
        }

        public void setCategoryname(String categoryname) {
            this.categoryname = categoryname;
        }

        public int getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(int categoryid) {
            this.categoryid = categoryid;
        }

        public int getCategorypid() {
            return categorypid;
        }

        public void setCategorypid(int categorypid) {
            this.categorypid = categorypid;
        }
    }
}
