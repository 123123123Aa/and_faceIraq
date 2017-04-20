package com.ready4s.faceiraq.and_faceiraq.model;

/**
 * Created by user on 19.04.2017.
 */

public class ItemListModel {

    private String title;
    private int imageId;

    public ItemListModel(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "ItemListModel{" +
                "title=" + title + '\'' +
                ", imageId=" + imageId +
                '}';
    }
}
