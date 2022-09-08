package com.affinity.sk_facilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutusDetails {

    private long id;
    private String content;

    @SerializedName("id")
    public long getID() { return id; }
    @SerializedName("id")
    public void setID(long value) { this.id = value; }

    @SerializedName("content")
    public String getContent() { return content; }
    @SerializedName("content")
    public void setContent(String value) { this.content = value; }
}
