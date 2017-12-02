package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class SpecLink implements Serializable {
    private String suiteCode;
    private String gender;
    private String shape;
    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSuiteCode() {
        return suiteCode;
    }

    public void setSuiteCode(String suiteCode) {
        this.suiteCode = suiteCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }
}
