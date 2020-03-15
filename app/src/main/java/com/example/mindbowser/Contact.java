package com.example.mindbowser;

import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String contactName;
    @SerializedName("number")
    private String contactNumber;
    @SerializedName("photo")
    private String contactImage;
    @SerializedName("favorite")
    private boolean favorite;
    @SerializedName("deleted")
    private boolean deleted;



    public Contact(int id,String contactName, String contactNumber, String contactImage,boolean favorite, boolean deleted) {
        this.id = id;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactImage = contactImage;
        this.favorite = favorite;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favoriteStatus) {
        this.favorite = favoriteStatus;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deletedStatus) {
        this.deleted = deletedStatus;
    }


}
