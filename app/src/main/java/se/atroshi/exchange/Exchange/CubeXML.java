package se.atroshi.exchange.Exchange;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Farhad on 11/11/15.
 */
public class CubeXML implements Serializable {

    private Date date;
    private double rate;
    private String currency;
    private String img;

    public CubeXML(Date date, String currency, double rate){
        this.date = date;
        this.rate = rate;
        this.currency = currency;
    }

    /**
     * Get date
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set date
     * @param date date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get rate for the object
     * @return rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * Set rate
     * @param rate set rate value
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * Get currency
     * @return currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Set currency
     * @param currency set value (string) for currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Return all info abour this object
     * @return information about the object
     */
    @Override
    public String toString(){
        String info = "";
        info += "Date: " + this.date + " Currency: " + this.currency + " Rate: " + this.rate;
        return info;
    }
}
