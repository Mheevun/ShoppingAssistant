package com.mheev.helpthemshop.model;

/**
 * Created by mheev on 9/18/2016.
 */
public class SavedCurrency {

    private String symbol;
    private String desc;

    public SavedCurrency(String symbol, String desc) {
        this.symbol = symbol;
        this.desc = desc;
    }


    public String getSymbol() {
        return this.symbol;
    }

    public String getDesc() {
        return this.desc;
    }

    public static String [] symbols = {"$", "¢", "£", "¤", "฿", "¥"};

    public static SavedCurrency[] MyCurrencyAll = {
            new SavedCurrency("$", "dollar sign"),
            new SavedCurrency("¢", "cent sign"),
            new SavedCurrency("£", "pound sign"),
            new SavedCurrency("¤", "currency sign"),
            new SavedCurrency("¥", "yen sign"),
            new SavedCurrency("ƒ", "latin small letter f with hook"),
            new SavedCurrency("", "afghani sign"),
            new SavedCurrency("৲", "bengali rupee mark"),
            new SavedCurrency("૱", "gujarati rupee sign"),
            new SavedCurrency("௹", "tamil rupee sign"),
            new SavedCurrency("฿", "thai currency symbol baht"),
            new SavedCurrency("¤", "khmer currency symbol riel"),
            new SavedCurrency("ℳ", "script capital m"),
            new SavedCurrency("元", "cjk unified ideograph-5143"),
            new SavedCurrency("円", "cjk unified ideograph-5186"),
            new SavedCurrency("圆", "cjk unified ideograph-5706"),
            new SavedCurrency("圓", "cjk unified ideograph-5713"),
            new SavedCurrency("", "rial sign"),
            new SavedCurrency("₠", "EURO-CURRENCY SIGN"),
            new SavedCurrency("₡", "COLON SIGN"),
            new SavedCurrency("₢", "CRUZEIRO SIGN"),
            new SavedCurrency("₣", "FRENCH FRANC SIGN"),
            new SavedCurrency("₤", "LIRA SIGN"),
            new SavedCurrency("₥", "MILL SIGN"),
            new SavedCurrency("₦", "NAIRA SIGN"),
            new SavedCurrency("₧", "PESETA SIGN"),
            new SavedCurrency("₨", "RUPEE SIGN"),
            new SavedCurrency("₩", "WON SIGN"),
            new SavedCurrency("₪", "NEW SHEQEL SIGN"),
            new SavedCurrency("₫", "DONG SIGN"),
            new SavedCurrency("€", "EURO SIGN"),
            new SavedCurrency("₭", "KIP SIGN"),
            new SavedCurrency("₮", "TUGRIK SIGN"),
            new SavedCurrency("₯", "DRACHMA SIGN"),
            new SavedCurrency("₰", "GERMAN PENNY SIGN"),
            new SavedCurrency("₱", "PESO SIGN"),
            new SavedCurrency("₲", "GUARANI SIGN"),
            new SavedCurrency("₳", "AUSTRAL SIGN"),
            new SavedCurrency("₴", "HRYVNIA SIGN"),
            new SavedCurrency("₵", "CEDI SIGN"),
            new SavedCurrency("₶", "LIVRE TOURNOIS SIGN"),
            new SavedCurrency("₷", "SPESMILO SIGN"),
            new SavedCurrency("₸", "TENGE SIGN"),
            new SavedCurrency("₹", "INDIAN RUPEE SIGN"),
            new SavedCurrency("₺", "TURKISH LIRA SIGN")
    };
}