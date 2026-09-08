package ru.rmdm.waifupics.model;

import com.google.gson.annotations.SerializedName;

public class WaifuImage {

    private int imageId;
    @SerializedName("url")
    private String url;
    private int height;
    private int width;
    @SerializedName("isNsfw")
    private boolean isNsfw;

    public String getUrl() {
        return url;
    }

}
