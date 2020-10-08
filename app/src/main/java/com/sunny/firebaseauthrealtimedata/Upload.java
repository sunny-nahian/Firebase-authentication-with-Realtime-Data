package com.sunny.firebaseauthrealtimedata;

import com.google.firebase.database.Exclude;

public class Upload {
    private String imageName;
    private String imageUrl;

    private String key; // for image delete, update etc
    //thn geter & seter

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    } // end key

    public Upload(){
    }

    public Upload(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
