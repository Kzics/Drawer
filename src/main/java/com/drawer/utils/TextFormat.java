package com.drawer.utils;

public class TextFormat {


    private final String text;
    public TextFormat(String text){
        this.text = text;
    }


    public int asInt(){
        return (parseAmount(text));
    }

    public String asString(){
        return text;
    }


    private int parseAmount(String amount) {
        if (amount.endsWith("k")) {
            String numberPart = amount.substring(0, amount.length() - 1).replace(",",".");
            double number = Double.parseDouble(numberPart);
            return (int) (number * 1000);
        } else {
            return Integer.parseInt(amount);
        }
    }

}
