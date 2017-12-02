package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class Pattern implements Serializable {
    private String pattn;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPattn() {
        return pattn;
    }

    public void setPattn(String pattn) {
        this.pattn = pattn;
    }
}
