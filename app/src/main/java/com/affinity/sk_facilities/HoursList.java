package com.affinity.sk_facilities;

import com.affinity.sk_facilities.activity.HourEstDtls;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HoursList {

    @SerializedName("results")
    @Expose
    private ArrayList<HourEstDtls> hourEstDtls = null;

    public ArrayList<HourEstDtls> getHourEstDtls() {
        return hourEstDtls;
    }

    public void setHourEstDtls(ArrayList<HourEstDtls> hourEstDtls) {
        this.hourEstDtls = hourEstDtls;
    }
}
