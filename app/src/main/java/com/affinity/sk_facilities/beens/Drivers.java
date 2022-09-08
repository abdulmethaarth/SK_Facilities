package com.affinity.sk_facilities.beens;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.affinity.sk_facilities.BaseResponse;

public class Drivers extends BaseResponse {

    @SerializedName("response")
    @Expose
    //private DriverDetails driverDetails;
    private ArrayList<DriverDetails> driverDetails = null;

    public ArrayList<DriverDetails> getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(ArrayList<DriverDetails> driverDetails) {
        this.driverDetails = driverDetails;
    }

    public class DriverDetails{

        @SerializedName("latitude")
        @Expose
        private String latitude;

        @SerializedName("longitude")
        @Expose
        private String longitude;

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}
