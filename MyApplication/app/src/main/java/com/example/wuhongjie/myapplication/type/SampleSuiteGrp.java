package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class SampleSuiteGrp implements Serializable {
    private String sampleSuiteId;
    private String sampleSuiteName;
    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSampleSuiteId() {
        return sampleSuiteId;
    }

    public void setSampleSuiteId(String sampleSuiteId) {
        this.sampleSuiteId = sampleSuiteId;
    }

    public String getSampleSuiteName() {
        return sampleSuiteName;
    }

    public void setSampleSuiteName(String sampleSuiteName) {
        this.sampleSuiteName = sampleSuiteName;
    }
}
