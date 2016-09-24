package com.mheev.helpthemshop.model.pojo;

/**
 * Created by mheev on 9/11/2016.
 */
public class Quantity {
    private int amount;
    private String unit;

    //TODO: unit to enum
    public Quantity(int amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
