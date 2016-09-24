package com.mheev.helpthemshop.model.api;

/**
 * Created by mheev on 9/21/2016.
 */

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Generated("org.jsonschema2pojo")
public class ApiEditResponse {

    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
