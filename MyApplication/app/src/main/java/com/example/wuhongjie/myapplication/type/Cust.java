package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class Cust implements Serializable {
    private String custCode;
    private String custName;
    private String areaCode;
    private boolean isSelected;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
