package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

    public class Team implements Serializable {
        private String teamId;
        private String suiteCode;
        private String termName;
        private String defaultValue;
        private String maxValue;
        private String minValue;
        private String stepValue;
        private String sort;
        private String currValue;
        private String groupValue;
        private String description;
        private boolean isSelected;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getSuiteCode() {
        return suiteCode;
    }

    public void setSuiteCode(String suiteCode) {
        this.suiteCode = suiteCode;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getStepValue() {
        return stepValue;
    }

    public void setStepValue(String stepValue) {
        this.stepValue = stepValue;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCurrValue() {
        return currValue;
    }

    public void setCurrValue(String currValue) {
        this.currValue = currValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }
}
