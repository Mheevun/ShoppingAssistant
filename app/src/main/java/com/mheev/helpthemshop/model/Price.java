package com.mheev.helpthemshop.model;

/**
 * Created by mheev on 9/17/2016.
 */
public class Price {
    private int value;
    private String currency;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
