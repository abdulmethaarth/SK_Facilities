package com.affinity.sk_facilities;

import com.affinity.sk_facilities.activity.HourEstDtls;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FetchOutStation extends BaseResponse {
    @SerializedName("results")
    @Expose
    private ArrayList<FetchOutStationDtls> fetchOutStationDtls = null;

    public ArrayList<FetchOutStationDtls> getFetchOutStationDtls() {
        return fetchOutStationDtls;
    }

    public void setFetchOutStationDtls(ArrayList<FetchOutStationDtls> fetchOutStationDtls) {
        this.fetchOutStationDtls = fetchOutStationDtls;
    }
}
