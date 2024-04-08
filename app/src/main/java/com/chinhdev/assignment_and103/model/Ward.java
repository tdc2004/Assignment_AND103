package com.chinhdev.assignment_and103.model;

public class Ward {
    private String WardCode;
    private int DistricID;
    private String WardName;

    public Ward(String wardCode, int districID, String wardName) {
        WardCode = wardCode;
        DistricID = districID;
        WardName = wardName;
    }

    public String getWardCode() {
        return WardCode;
    }

    public void setWardCode(String wardCode) {
        WardCode = wardCode;
    }

    public int getDistricID() {
        return DistricID;
    }

    public void setDistricID(int districID) {
        DistricID = districID;
    }

    public String getWardName() {
        return WardName;
    }

    public void setWardName(String wardName) {
        WardName = wardName;
    }
}
