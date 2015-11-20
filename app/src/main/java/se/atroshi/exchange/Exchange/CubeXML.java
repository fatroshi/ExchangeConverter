package se.atroshi.exchange.Exchange;

import java.io.Serializable;

/**
 * Created by Farhad on 11/11/15.
 */
public class CubeXML implements Serializable {

    private String date;
    private double rate;
    private String currency;

    public CubeXML(String date, String currency, double rate){
        this.date = date;
        this.rate = rate;
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    @Override
    public String toString(){
        String info = "";
        info += "Date: " + this.date + " Currency: " + this.currency + " Rate: " + this.rate;
        return info;
    }
}
