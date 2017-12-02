package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class Spec implements Serializable {
    private String grpId;
    private String specId;
    private String suiteCode;
    private String gender;
    private String specCode;
    private String pattern;
    private String spec;
    private String shape;
    private String length;
    private String width;
    private boolean isSelected;

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
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

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }
}
