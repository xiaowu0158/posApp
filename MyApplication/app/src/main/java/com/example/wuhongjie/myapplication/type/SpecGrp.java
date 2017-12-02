package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class SpecGrp implements Serializable {
    private String grpId;
    private String suiteCode;
    private String grpCode;
    private String grpName;
    private boolean isSelected;
    private ArrayList<Spec> specs;
    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getSuiteCode() {
        return suiteCode;
    }

    public void setSuiteCode(String suiteCode) {
        this.suiteCode = suiteCode;
    }

    public String getGrpCode() {
        return grpCode;
    }

    public void setGrpCode(String grpCode) {
        this.grpCode = grpCode;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<Spec> getSpecs() {
        return specs;
    }

    public void setSpecs(ArrayList<Spec> specs) {
        this.specs = specs;
    }
}
