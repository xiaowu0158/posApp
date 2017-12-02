package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class SuiteGrp implements Serializable {
    private String suiteGrpId;
    private String catId;
    private String suiteGrpName;
    private String seqNum;
    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSuiteGrpId() {
        return suiteGrpId;
    }

    public void setSuiteGrpId(String suiteGrpId) {
        this.suiteGrpId = suiteGrpId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSuiteGrpName() {
        return suiteGrpName;
    }

    public void setSuiteGrpName(String suiteGrpName) {
        this.suiteGrpName = suiteGrpName;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }
}
