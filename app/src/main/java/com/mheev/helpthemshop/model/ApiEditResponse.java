package com.mheev.helpthemshop.model;

/**
 * Created by mheev on 9/21/2016.
 */

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ApiEditResponse {

    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("editAt")
    @Expose
    private String editAt;

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

    public String getEditAt() {
        return editAt;
    }

    public void setEditAt(String editAt) {
        this.editAt = editAt;
    }
}
