package com.example.wuhongjie.myapplication.type;

import com.evangelsoft.econnect.dataformat.CharacterCase;
import com.evangelsoft.econnect.dataformat.RecordFieldFormat;
import com.evangelsoft.econnect.dataformat.RecordFormat;

import java.io.Serializable;

/**
 * Created by wuhongjie on 2017-02-28.
 */

public class Employee implements Serializable {
    private String pckNo;//ID
    private String dept;
    private String cname;
    private String gender;
    private boolean isSelected;
    private boolean isLted;
    private int prdCnt;
    public String getPckNo() {
        return pckNo;
    }

    public void setPckNo(String pckNo) {
        this.pckNo = pckNo;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isLted() {
        return isLted;
    }

    public void setLted(boolean lted) {
        isLted = lted;
    }

    public int getPrdCnt() {
        return prdCnt;
    }

    public void setPrdCnt(int prdCnt) {
        this.prdCnt = prdCnt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public RecordFormat getEmpRecordFormat(){
        RecordFormat format = new RecordFormat("@");
        format.clearFields();
        format.appendField(new RecordFieldFormat("PCKNO", "ID号",
                RecordFieldFormat.TYPE_VARCHAR, 10, 0, 10,
                CharacterCase.normal));
        format.appendField(new RecordFieldFormat("DEPT", "部门",
                RecordFieldFormat.TYPE_VARCHAR, 60, 0, 20,
                CharacterCase.normal));
        format.appendField(new RecordFieldFormat("GENDER", "性别",
                RecordFieldFormat.TYPE_VARCHAR, 60, 0, 20,
                CharacterCase.normal));
        format.appendField(new RecordFieldFormat("CNAME", "姓名",
                RecordFieldFormat.TYPE_VARCHAR, 20, 0, 20,
                CharacterCase.normal));
        format.appendField(new RecordFieldFormat("STATUS", "状态",
                RecordFieldFormat.TYPE_VARCHAR, 10, 0, 20,
                CharacterCase.normal));
        return format;
    }

}
