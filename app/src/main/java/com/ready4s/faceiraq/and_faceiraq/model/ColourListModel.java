package com.ready4s.faceiraq.and_faceiraq.model;

/**
 * Created by user on 20.04.2017.
 */

public class ColourListModel {

    private String title;
    private int colourId;
    private boolean isSelected;
    private String themeColour;

    public ColourListModel(String title, int colourId, boolean isSelected, String themeColour) {
        this.title = title;
        this.colourId = colourId;
        this.isSelected = isSelected;
        this.themeColour = themeColour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColourId() {
        return colourId;
    }

    public void setColourId(int colourId) {
        this.colourId = colourId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getThemeColour() {
        return themeColour;
    }

    public void setThemeColour(String themeColour) {
        this.themeColour = themeColour;
    }

    @Override
    public String toString() {
        return "ColourListModel{" +
                "title=" + title + '\'' +
                ", colourId=" + colourId +
                ", isSelected=" + isSelected +
                "themeColour=" + themeColour + '\'' +
                '}';
    }
}


