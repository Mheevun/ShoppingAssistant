package com.mheev.helpthemshop.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mheev on 9/11/2016.
 */
public class ShoppingItem {
    @Expose(serialize = false, deserialize = false)
    public int position;

    private String itemName;
    private String itemDescription;

    @SerializedName("objectId")
    private String id;

    private List<String> tags; //todo hashtag
    private String group;
    private String note;
    private Quantity quantity;
    private Price price;
    private String author;

    public String getItemAvatarURL() {
        return itemAvatarURL;
    }

    public void setItemAvatarURL(String itemAvatarURL) {
        this.itemAvatarURL = itemAvatarURL;
    }

    public String getItemWallpaperURL() {
        return itemWallpaperURL;
    }

    public void setItemWallpaperURL(String itemWallpaperURL) {
        this.itemWallpaperURL = itemWallpaperURL;
    }

    private String itemAvatarURL;
    private String itemWallpaperURL;
    private boolean favorite;

    private Date test;

    public ShoppingItem() {

    }



    public ShoppingItem(String itemName){
        itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
        this.itemName = itemName;
    }

    public ShoppingItem(String itemName, Quantity quantity) {
        this(itemName);
        this.quantity = quantity;
    }


    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public boolean isNew(){
        if(id==null)
            return true;
        else
            return false;
    }
}
