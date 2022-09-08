package com.affinity.sk_facilities;

import com.affinity.sk_facilities.beens.TripDetails;
import com.affinity.sk_facilities.beens.Users;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AboutUsContent extends BaseResponse{

    @SerializedName("userdetails")
    @Expose
    /*private AboutusDetails userDetails;

    public AboutusDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(AboutusDetails userDetails) {
        this.userDetails = userDetails;
    }*/

     private ArrayList<AboutusDetails> userdetails = null;

    public ArrayList<AboutusDetails> getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(ArrayList<AboutusDetails> userdetails) {
        this.userdetails = userdetails;
    }
}
