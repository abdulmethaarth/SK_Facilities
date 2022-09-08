package com.affinity.sk_facilities.beens;

import com.google.gson.annotations.SerializedName;

public class Banner {

   /* @SerializedName("image")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }*/

    private int pic = -1 ;
    private String name = "" ;

    public Banner(int pic, String name){
        this.pic = pic ;
        this.name = name ;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

