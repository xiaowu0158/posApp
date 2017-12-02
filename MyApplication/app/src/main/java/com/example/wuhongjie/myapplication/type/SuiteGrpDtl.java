package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class SuiteGrpDtl implements Serializable {

    private String suiteGrpId;
    private String suiteGrpName;
    private String suiteSort;
    private String suiteCode;
    private String suiteName;
    private String termMemoRst;
    public String getDtlId() {
        return dtlId;
    }

    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }

    private String dtlId;
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

    public String getSuiteGrpName() {
        return suiteGrpName;
    }

    public void setSuiteGrpName(String suiteGrpName) {
        this.suiteGrpName = suiteGrpName;
    }

    public String getSuiteSort() {
        return suiteSort;
    }

    public void setSuiteSort(String suiteSort) {
        this.suiteSort = suiteSort;
    }

    public String getSuiteCode() {
        return suiteCode;
    }

    public void setSuiteCode(String suiteCode) {
        this.suiteCode = suiteCode;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getTermMemoRst() {
        return termMemoRst;
    }

    public void setTermMemoRst(String termMemoRst) {
        this.termMemoRst = termMemoRst;
    }
}
