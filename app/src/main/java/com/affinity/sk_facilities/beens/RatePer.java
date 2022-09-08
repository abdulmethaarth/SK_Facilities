package com.affinity.sk_facilities.beens;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.affinity.sk_facilities.BaseResponse;

public class RatePer extends BaseResponse {

    @SerializedName("results")
    @Expose
    private RateDetails Rate_Details;

    public RateDetails getRate_Details() {
        return Rate_Details;
    }

    public void setRate_Details(RateDetails rate_Details) {
        Rate_Details = rate_Details;
    }

    public class RateDetails {

        @SerializedName("distance")
        @Expose
        private String distance;

        @SerializedName("duration")
        @Expose
        private String duration;

        @SerializedName("origin")
        @Expose
        private String origin;

        @SerializedName("destination")
        @Expose
        private String destination;

        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("carname")
        @Expose
        private String carname;

         @SerializedName("totalCharge")
        @Expose
        private String totalCharge;

         @SerializedName("perkm")
        @Expose
        private String perkm;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCarname() {
            return carname;
        }

        public void setCarname(String carname) {
            this.carname = carname;
        }

        public String getTotalCharge() {
            return totalCharge;
        }

        public void setTotalCharge(String totalCharge) {
            this.totalCharge = totalCharge;
        }

        public String getPerkm() {
            return perkm;
        }

        public void setPerkm(String perkm) {
            this.perkm = perkm;
        }
    }
}
