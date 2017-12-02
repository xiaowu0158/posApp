package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class Area implements Serializable {
    private String quyDm;
    private String quyMc;
    private String yxgsDm;
    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getYxgsDm() {
        return yxgsDm;
    }
    public void setYxgsDm(String yxgsDm) {
        this.yxgsDm = yxgsDm;
    }

    public String getQuyDm() {
        return quyDm;
    }

    public void setQuyDm(String quyDm) {
        this.quyDm = quyDm;
    }

    public String getQuyMc() {
        return quyMc;
    }

    public void setQuyMc(String quyMc) {
        this.quyMc = quyMc;
    }
}
