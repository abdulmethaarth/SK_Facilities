package com.affinity.sk_facilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoundTripHourEstDtls extends BaseResponse {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("perMinute")
    @Expose
    private String perMinute;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("offerprice")
    @Expose
    private String offerprice;

    @SerializedName("hourse")
    @Expose
    private String hourse;

    @SerializedName("roundID")
    @Expose
    private String roundID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerMinute() {
        return perMinute;
    }

    public void setPerMinute(String perMinute) {
        this.perMinute = perMinute;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferprice() {
        return offerprice;
    }

    public void setOfferprice(String offerprice) {
        this.offerprice = offerprice;
    }

    public String getHourse() {
        return hourse;
    }

    public void setHourse(String hourse) {
        this.hourse = hourse;
    }

    public String getRoundID() {
        return roundID;
    }

    public void setRoundID(String roundID) {
        this.roundID = roundID;
    }
}
