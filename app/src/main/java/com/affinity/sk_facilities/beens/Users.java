package com.affinity.sk_facilities.beens;

import com.affinity.sk_facilities.RideTypeDtls;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.affinity.sk_facilities.BaseResponse;

import java.util.ArrayList;

public class  Users extends BaseResponse {

    @SerializedName("userdetails")
    @Expose
    private ArrayList<LoginUserDetails> userDetails = null;

    public ArrayList<LoginUserDetails> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(ArrayList<LoginUserDetails> userDetails) {
        this.userDetails = userDetails;
    }

    public class LoginUserDetails{

        @SerializedName("user_id")
        @Expose
        private String user_id;


        @SerializedName("name")
        @Expose
        private String name;


        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("phone")
        @Expose
        private String phone;



        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}

