package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class Suite implements Serializable {

    private String suiteCode;
    private String suiteName;
    private String teamMemo;
    private String gender;
    private String prodCatId;
    private String prodCatName;
    private SpecGrp specGrp;//当前规格组
    private Spec spec;//当前规格
    private boolean isSelected;
    private boolean isCollapsible;
    private ArrayList<TeamGroup> teamGroup;
    private ArrayList<SpecGrp> specGrps;

    public String getProdCatId() {
        return prodCatId;
    }

    public void setProdCatId(String prodCatId) {
        this.prodCatId = prodCatId;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

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

    public String getTeamMemo() {
        return teamMemo;
    }

    public void setTeamMemo(String teamMemo) {
        this.teamMemo = teamMemo;
    }

    public ArrayList<TeamGroup> getTeamGroup() {
        return teamGroup;
    }

    public void setTeamGroup(ArrayList<TeamGroup> teamGroup) {
        this.teamGroup = teamGroup;
    }

    public ArrayList<SpecGrp> getSpecGrps() {
        return specGrps;
    }

    public void setSpecGrps(ArrayList<SpecGrp> specGrps) {
        this.specGrps = specGrps;
    }

    public SpecGrp getSpecGrp() {
        return specGrp;
    }

    public void setSpecGrp(SpecGrp specGrp) {
        this.specGrp = specGrp;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public String getProdCatName() {
        return prodCatName;
    }

    public void setProdCatName(String prodCatName) {
        this.prodCatName = prodCatName;
    }

    public boolean isCollapsible() {
        return isCollapsible;
    }

    public void setCollapsible(boolean collapsible) {
        isCollapsible = collapsible;
    }
}
