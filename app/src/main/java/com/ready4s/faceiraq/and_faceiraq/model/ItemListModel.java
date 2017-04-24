package com.ready4s.faceiraq.and_faceiraq.model;

/**
 * Created by user on 19.04.2017.
 */

public class ItemListModel {

    private String title;
    private int imageId;
    private boolean isSelected;

    public ItemListModel(String title, int imageId, boolean isSelected) {
        this.title = title;
        this.imageId = imageId;
        this.isSelected = isSelected;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "ItemListModel{" +
                "title=" + title + '\'' +
                ", imageId=" + imageId +
                ", isSelected=" + isSelected +
                '}';
    }
}
