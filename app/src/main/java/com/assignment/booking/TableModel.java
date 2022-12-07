package com.assignment.booking;

public class TableModel {

    public String name;
    public String location;
    public int numOfGuests;
    public String description;
    public String imgURL;

    //Default Constructor
    public TableModel() {}

    public TableModel(String name, String location, int numOfGuests, String description, String imgURL) {
        this.name = name;
        this.location = location;
        this.numOfGuests = numOfGuests;
        this.description = description;
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
