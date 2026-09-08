package ru.rmdm.waifupics.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WaifuResponse {

    @SerializedName("items")
    private List<WaifuImage> images;

    public List<WaifuImage> getImages() {
        return images;
    }

    public void setImages(List<WaifuImage> images) {
        this.images = images;
    }
}
