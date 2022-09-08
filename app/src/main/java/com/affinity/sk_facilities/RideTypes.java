package com.affinity.sk_facilities;

import com.affinity.sk_facilities.activity.HourEstDtls;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RideTypes {

    @SerializedName("results")
    @Expose
    private ArrayList<RideTypeDtls> rideTypeDtls = null;

    public ArrayList<RideTypeDtls> getRideTypeDtls() {
        return rideTypeDtls;
    }

    public void setRideTypeDtls(ArrayList<RideTypeDtls> rideTypeDtls) {
        this.rideTypeDtls = rideTypeDtls;
    }
}
