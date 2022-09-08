package com.affinity.sk_facilities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import com.affinity.sk_facilities.beens.Banner;

public class Banners    {

    @SerializedName("screenList")
    private ArrayList<Banner> banners;

    public Banners(){ }

    public ArrayList<Banner> getBanners(){
        return banners;
    }
}
