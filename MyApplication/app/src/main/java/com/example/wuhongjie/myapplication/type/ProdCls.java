package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class ProdCls implements Serializable {

    private String ordNo;
    private String prdCode;
    private String prdName;
    private String prdMemo;
    private String prodCatId;
    private String prodCatName;
    private String suiteGrpId;
    private String suiteGrpName;
    private String imageUrl;
    private String imageFileName;
    private String gender;
    private String suiteCode;
    private String teamMemo;
    private String uom;
    private String qty;
    private boolean isSelected;
    private boolean isSimple;
    public String getProdCatId() {
        return prodCatId;
    }

    public void setProdCatId(String prodCatId) {
        this.prodCatId = prodCatId;
    }


    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }

    public String getPrdCode() {
        return prdCode;
    }

    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public String getPrdMemo() {
        return prdMemo;
    }

    public void setPrdMemo(String prdMemo) {
        this.prdMemo = prdMemo;
    }

    public String getSuiteGrpId() {
        return suiteGrpId;
    }

    public void setSuiteGrpId(String suiteGrpId) {
        this.suiteGrpId = suiteGrpId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSuiteGrpName() {
        return suiteGrpName;
    }

    public void setSuiteGrpName(String suiteGrpName) {
        this.suiteGrpName = suiteGrpName;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getProdCatName() {
        return prodCatName;
    }

    public void setProdCatName(String prodCatName) {
        this.prodCatName = prodCatName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public boolean isSimple() {
        return isSimple;
    }

    public void setSimple(boolean simple) {
        isSimple = simple;
    }
}
