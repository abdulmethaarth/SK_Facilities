package com.affinity.sk_facilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchOutStationDtls extends BaseResponse{

    @SerializedName("name")
    @Expose
    private String name;

   /* @SerializedName("buttonID")
    @Expose
    private String buttonID;

    @SerializedName("outstationID")
    @Expose
    private String outstationID;*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* public String getButtonID() {
        return buttonID;
    }

    public void setButtonID(String buttonID) {
        this.buttonID = buttonID;
    }

    public String getOutstationID() {
        return outstationID;
    }

    public void setOutstationID(String outstationID) {
        this.outstationID = outstationID;
    }*/
}
