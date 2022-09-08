package com.affinity.sk_facilities.activity;

import com.affinity.sk_facilities.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HourEstDtls extends BaseResponse {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("timecal")
    @Expose
    private String timecal;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimecal() {
        return timecal;
    }

    public void setTimecal(String timecal) {
        this.timecal = timecal;
    }
}
