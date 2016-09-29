package com.mheev.helpthemshop.model.pojo;

/**
 * Created by mheev on 9/17/2016.
 */
public class Price {
    private int value;
    private String currency =  "฿";

    public Price(){

    }

    public Price(String valueStr, String unit) {
        if(valueStr==null){
            this.value = 0;
        }else
            this.value = Integer.parseInt(valueStr);

        this.currency = unit;
    }

    public Price(int value, String currency) {
        this.value = value;
        this.currency = currency;
    }

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
