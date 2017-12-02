package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class Yxgs implements Serializable {
    private String yxgsDm;
    private String yxgsMc;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;
    public String getYxgsDm() {
        return yxgsDm;
    }

    public void setYxgsDm(String yxgsDm) {
        this.yxgsDm = yxgsDm;
    }

    public String getYxgsMc() {
        return yxgsMc;
    }

    public void setYxgsMc(String yxgsMc) {
        this.yxgsMc = yxgsMc;
    }
}
