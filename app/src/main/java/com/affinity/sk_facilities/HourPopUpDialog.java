package com.affinity.sk_facilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HourPopUpDialog extends BaseResponse{

    @SerializedName("results")
    @Expose
    private HourPopDetails hourPopDetails;

    public HourPopDetails getHourPopDetails() {
        return hourPopDetails;
    }

    public void setHourPopDetails(HourPopDetails hourPopDetails) {
        this.hourPopDetails = hourPopDetails;
    }

    public class HourPopDetails{
        private String distance;
        private String duration;
        private String origin;
        private String destination;
        private String mode;
        private String units;
        private String language;
        private Object avoid;
        private String type;
        private String gst;
        private String onewayCharge;
        private String perkm;
        private String perHour;
        private String totalRiding;
        private String totalKM;
        private String payingKM;
        private String totalCharge;
        private String gstAmount;
        private String totalwithgst;

        @SerializedName("distance")
        public String getDistance() { return distance; }
        @SerializedName("distance")
        public void setDistance(String value) { this.distance = value; }


        @SerializedName("duration")
        public String getDuration() { return duration; }
        @SerializedName("duration")
        public void setDuration(String value) { this.duration = value; }


        @SerializedName("origin")
        public String getOrigin() { return origin; }
        @SerializedName("origin")
        public void setOrigin(String value) { this.origin = value; }

        @SerializedName("destination")
        public String getDestination() { return destination; }
        @SerializedName("destination")
        public void setDestination(String value) { this.destination = value; }

        @SerializedName("mode")
        public String getMode() { return mode; }
        @SerializedName("mode")
        public void setMode(String value) { this.mode = value; }

        @SerializedName("units")
        public String getUnits() { return units; }
        @SerializedName("units")
        public void setUnits(String value) { this.units = value; }

        @SerializedName("language")
        public String getLanguage() { return language; }
        @SerializedName("language")
        public void setLanguage(String value) { this.language = value; }

        @SerializedName("avoid")
        public Object getAvoid() { return avoid; }
        @SerializedName("avoid")
        public void setAvoid(Object value) { this.avoid = value; }

        @SerializedName("type")
        public String getType() { return type; }
        @SerializedName("type")
        public void setType(String value) { this.type = value; }

        @SerializedName("gst")
        public String getGst() { return gst; }
        @SerializedName("gst")
        public void setGst(String value) { this.gst = value; }

        @SerializedName("onewayCharge")
        public String getOnewayCharge() { return onewayCharge; }
        @SerializedName("onewayCharge")
        public void setOnewayCharge(String value) { this.onewayCharge = value; }

        @SerializedName("perkm")
        public String getPerkm() { return perkm; }
        @SerializedName("perkm")
        public void setPerkm(String value) { this.perkm = value; }

        @SerializedName("perHour")
        public String getPerHour() { return perHour; }
        @SerializedName("perHour")
        public void setPerHour(String value) { this.perHour = value; }

        @SerializedName("totalRiding")
        public String getTotalRiding() { return totalRiding; }
        @SerializedName("totalRiding")
        public void setTotalRiding(String value) { this.totalRiding = value; }

        @SerializedName("totalKm")
        public String getTotalKM() { return totalKM; }
        @SerializedName("totalKm")
        public void setTotalKM(String value) { this.totalKM = value; }

        @SerializedName("payingKm")
        public String getPayingKM() { return payingKM; }
        @SerializedName("payingKm")
        public void setPayingKM(String value) { this.payingKM = value; }

        @SerializedName("totalCharge")
        public String getTotalCharge() { return totalCharge; }
        @SerializedName("totalCharge")
        public void setTotalCharge(String value) { this.totalCharge = value; }

        @SerializedName("gstAmount")
        public String getGstAmount() { return gstAmount; }
        @SerializedName("gstAmount")
        public void setGstAmount(String value) { this.gstAmount = value; }

        @SerializedName("totalwithgst")
        public String getTotalwithgst() { return totalwithgst; }
        @SerializedName("totalwithgst")
        public void setTotalwithgst(String value) { this.totalwithgst = value; }
       /* @SerializedName("gstAmount")
        @Expose
        private String gstAmount;

        @SerializedName("totalwithgst")
        @Expose
        private String totalwithgst;

        @SerializedName("totalCharge")
        @Expose
        private String totalCharge;

        @SerializedName("payingKm")
        @Expose
        private String payingKm;

        @SerializedName("perHour")
        @Expose
        private String perHour;

        @SerializedName("perkm")
        @Expose
        private String perkm;

        @SerializedName("gst")
        @Expose
        private String gst;

        @SerializedName("onewayCharge")
        @Expose
        private String onewayCharge;

        public String getGstAmount() {
            return gstAmount;
        }

        public void setGstAmount(String gstAmount) {
            this.gstAmount = gstAmount;
        }

        public String getTotalwithgst() {
            return totalwithgst;
        }

        public void setTotalwithgst(String totalwithgst) {
            this.totalwithgst = totalwithgst;
        }

        public String getTotalCharge() {
            return totalCharge;
        }

        public void setTotalCharge(String totalCharge) {
            this.totalCharge = totalCharge;
        }

        public String getPayingKm() {
            return payingKm;
        }

        public void setPayingKm(String payingKm) {
            this.payingKm = payingKm;
        }

        public String getPerHour() {
            return perHour;
        }

        public void setPerHour(String perHour) {
            this.perHour = perHour;
        }

        public String getPerkm() {
            return perkm;
        }

        public void setPerkm(String perkm) {
            this.perkm = perkm;
        }

        public String getGst() {
            return gst;
        }

        public void setGst(String gst) {
            this.gst = gst;
        }

        public String getOnewayCharge() {
            return onewayCharge;
        }

        public void setOnewayCharge(String onewayCharge) {
            this.onewayCharge = onewayCharge;
        }*/
    }
}
