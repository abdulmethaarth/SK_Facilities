package com.affinity.sk_facilities;

import com.google.gson.annotations.SerializedName;

public class NearestDriverTiming {

    @SerializedName("time")
    public String time;

    @SerializedName("status")
    public String status;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
