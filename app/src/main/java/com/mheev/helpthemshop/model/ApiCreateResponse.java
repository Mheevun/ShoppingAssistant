package com.mheev.helpthemshop.model;

/**
 * Created by mheev on 9/21/2016.
 */

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ApiCreateResponse {

    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    /**
     *
     * @return
     * The objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     *
     * @param objectId
     * The objectId
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The createdAt
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
