package com.affinity.sk_facilities.beens;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.affinity.sk_facilities.BaseResponse;

public class CarType extends BaseResponse {
    @SerializedName("carname")
    @Expose
    private String carname;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("carimage")
    @Expose
    private String carimage;

    @SerializedName("type")
    @Expose
    private String type;


    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarimage() {
        return carimage;
    }

    public void setCarimage(String carimage) {
        this.carimage = carimage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
