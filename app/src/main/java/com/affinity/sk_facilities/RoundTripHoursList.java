package com.affinity.sk_facilities;

import com.affinity.sk_facilities.activity.HourEstDtls;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RoundTripHoursList {

    @SerializedName("results")
    @Expose
    private ArrayList<RoundTripHourEstDtls> roundTripHourEstDtls = null;

    public ArrayList<RoundTripHourEstDtls> getRoundTripHourEstDtls() {
        return roundTripHourEstDtls;
    }

    public void setRoundTripHourEstDtls(ArrayList<RoundTripHourEstDtls> roundTripHourEstDtls) {
        this.roundTripHourEstDtls = roundTripHourEstDtls;
    }
}
