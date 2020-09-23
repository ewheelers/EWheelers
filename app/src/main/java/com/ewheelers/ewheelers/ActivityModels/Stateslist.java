package com.ewheelers.ewheelers.ActivityModels;

public class Stateslist {
    private String stateid;
    private String statecode;
    private String statename;

    public Stateslist(String stateid, String statename) {
        this.stateid = stateid;
        this.statename = statename;
    }

    public Stateslist(String stateid) {
        this.stateid = stateid;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }
    @Override
    public String toString() {
        return statename;
    }
}
