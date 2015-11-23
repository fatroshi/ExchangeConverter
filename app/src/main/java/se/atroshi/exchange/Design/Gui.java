package se.atroshi.exchange.Design;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;
import se.atroshi.exchange.Controller.MainController;
import se.atroshi.exchange.Exchange.CubeXML;
import se.atroshi.exchange.Listeners.FromSpinnerListener;
import se.atroshi.exchange.Listeners.ToSpinnerListener;
import se.atroshi.exchange.Listeners.UserInputListener;
import se.atroshi.exchange.MainActivity;
import se.atroshi.exchange.R;

/**
 * Created by Farhad on 17/11/15.
 */
public class Gui extends AppCompatActivity {


    private MainActivity mainActivity;
    private MainController controller;

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private TextView txtResult;
    private EditText txtQuantity;



    private String toCurrency;
    private double toRate;
    private double fromRate;
    private double quantity;

    // Store currency
    private final List<String> currencyList = new ArrayList<>();
    // Store rate
    private final List<Double> rateList = new ArrayList<>();
    // Store country flags (images)
    private List<Integer> imageList = new ArrayList<>();


    public Gui(MainActivity mainActivity, MainController controller){
        this.mainActivity = mainActivity;
        this.controller = controller;

        this.connectGuiElements();
    }


    public List<Double> getRateList(){
        return this.rateList;
    }

    public List<String> getCurrencyList(){
        return this.currencyList;
    }

    public void showResult(){
        double result = exchangeConvert();
        if(result > 0){
            result = roundDouble(result, 3);
            txtResult.setText("Converted: " + String.valueOf(result) + " " + getToCurrency());
        }else{
            txtResult.setText("");
        }

    }

    public Spinner getFromSpinner(){
        return this.fromSpinner;
    }

    public Spinner getToSpinner(){
        return this.toSpinner;
    }

    public double getToRate() {
        return toRate;
    }

    public void setToCurrency(String s){
        this.toCurrency = s;
    }

    public String getToCurrency(){
        return this.toCurrency;
    }

    public void setToRate(double toRate) {
        this.toRate = toRate;
    }

    public double getFromRate() {
        return fromRate;
    }

    public void setFromRate(double fromRate) {
        this.fromRate = fromRate;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void connectGuiElements(){
        this.txtResult = (TextView) this.mainActivity.findViewById(R.id.result);
        this.txtQuantity = (EditText) this.mainActivity.findViewById(R.id.inputText);

        this.fromSpinner = (Spinner) this.mainActivity.findViewById(R.id.fromSpinner);
        this.toSpinner = (Spinner) this.mainActivity.findViewById(R.id.toSpinner);

        txtQuantity.addTextChangedListener(new UserInputListener(this.controller,this));

    }


    public double exchangeConvert(){

        //double cash = 0;
        double result = 0;

        if(this.quantity > 0){
            result = (this.toRate / this.fromRate) * this.quantity;
        }

        return result;
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner(List<CubeXML> cubes) {

        // Add to the lists
        for(CubeXML cube: cubes){
            this.currencyList.add(cube.getCurrency());
            this.rateList.add(cube.getRate());
            if(!cube.getCurrency().matches("try")) {
                this.imageList.add(getDrawable(this.mainActivity, cube.getCurrency()));
            }
        }
        // Populate the spinners
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.mainActivity, android.R.layout.simple_spinner_item, currencyList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // My custom adapter
        CustomAdapter customAdapter = new CustomAdapter(this.mainActivity,this.currencyList,this.imageList);

        //fromSpinner.setAdapter(dataAdapter);
        fromSpinner.setAdapter(customAdapter);
        toSpinner.setAdapter(dataAdapter);

        // Get selected item in spinner list
        fromSpinner.setOnItemSelectedListener(new FromSpinnerListener(this.controller,this));

        // Get selected item in spinner list
        toSpinner.setOnItemSelectedListener(new ToSpinnerListener(this.controller, this));
    }

    /**
     * http://stackoverflow.com/questions/7948059/dynamic-loading-of-images-r-drawable-using-variable
     * @param context
     * @param name
     * @return
     */
    public static int getDrawable(Context context, String name)
    {
            Assert.assertNotNull(context);
            Assert.assertNotNull(name);
            return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }


    /**
     * http://stackoverflow.com/questions/11072576/set-selected-item-of-spinner-programmatically
     * @param spnr
     * @param value
     */
    public void selectSpinnerItemByValue(Spinner spnr, int value)
    {
        spnr.setSelection(value);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Found at: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
     *
     * @param value The value that we want to round
     * @param places Max decimals of the returned value
     * @return the value
     */
    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
