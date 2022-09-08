package com.affinity.sk_facilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideTypeDtls extends BaseResponse {

    @SerializedName("butttonID")
    @Expose
    private String butttonID;

    @SerializedName("type")
    @Expose
    private String type;

    public String getButttonID() {
        return butttonID;
    }

    public void setButttonID(String butttonID) {
        this.butttonID = butttonID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
