package se.atroshi.exchange.Design;
import android.support.v7.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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

    private double toRate;
    private double fromRate;
    private double quantity;


    public Gui(MainActivity mainActivity, MainController controller){
        this.mainActivity = mainActivity;
        this.controller = controller;
        this.connectElements();

    }

    public void showResult(){
        txtResult.setText(String.valueOf(exchangeConvert()));
    }

    public double getToRate() {
        return toRate;
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

    public void connectElements(){
        this.txtResult = (TextView) this.mainActivity.findViewById(R.id.result);
        this.txtQuantity = (EditText) this.mainActivity.findViewById(R.id.inputText);

        this.fromSpinner = (Spinner) this.mainActivity.findViewById(R.id.fromSpinner);
        this.toSpinner = (Spinner) this.mainActivity.findViewById(R.id.toSpinner);

        txtQuantity.addTextChangedListener(new UserInputListener(this.controller,this));

    }


    public double exchangeConvert(){

        double cash = 0;
        double result = 0;

        if(this.quantity > 0){
            cash = this.fromRate * this.quantity;
            //showToast("Cash: " + String.valueOf(cash));
            result = cash / this.toRate;
            showToast("Result: " + String.valueOf(result));
        }

        return result;
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner(List<CubeXML> cubes) {
        // Store currency
        final List<String> currencyList = new ArrayList<>();
        // Store rate
        final List<Double> rateList = new ArrayList<>();

        // Add to the lists
        for(CubeXML cube: cubes){
            currencyList.add(cube.getCurrency());
            rateList.add(cube.getRate());
        }
        // Populate the spinners
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.mainActivity, android.R.layout.simple_spinner_item, currencyList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(dataAdapter);
        toSpinner.setAdapter(dataAdapter);

        // Get selected item in spinner list
        fromSpinner.setOnItemSelectedListener(new FromSpinnerListener(this.fromRate,rateList,this.controller,this));

        // Get selected item in spinner list
        toSpinner.setOnItemSelectedListener(new ToSpinnerListener(this.toRate,rateList,this.controller,this));
    }


    /**
     * http://stackoverflow.com/questions/11072576/set-selected-item-of-spinner-programmatically
     * @param spnr
     * @param value
     */
    public static void selectSpinnerItemByValue(Spinner spnr, long value)
    {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++)
        {
            if(adapter.getItemId(position) == value)
            {
                spnr.setSelection(position);
                return;
            }
        }
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
