package com.mheev.helpthemshop.model;

/**
 * Created by mheev on 9/21/2016.
 */

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ApiQueryResponse {

    @SerializedName("results")
    @Expose
    private List<ShoppingItem> results = new ArrayList<ShoppingItem>();

    /**
     *
     * @return
     * The results
     */
    public List<ShoppingItem> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<ShoppingItem> results) {
        this.results = results;
    }

}
