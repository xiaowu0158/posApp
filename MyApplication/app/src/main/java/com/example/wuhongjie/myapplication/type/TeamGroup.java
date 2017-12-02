package com.example.wuhongjie.myapplication.type;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wuhongjie on 2017-02-28.
 */

    public class TeamGroup implements Serializable {
        private String groupId;
        private boolean isSelected;
        private ArrayList<Team> teams;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
