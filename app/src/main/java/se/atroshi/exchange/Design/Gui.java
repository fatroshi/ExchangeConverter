package se.atroshi.exchange.Design;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

import se.atroshi.exchange.Exchange.CubeXML;
import se.atroshi.exchange.MainActivity;
import se.atroshi.exchange.R;

/**
 * Created by Farhad on 17/11/15.
 */
public class Gui extends AppCompatActivity {

    Spinner fromSpinner;
    Spinner toSpinner;
    MainActivity mainActivity;
    Bundle bundle;

    public Gui(MainActivity mainActivity, Bundle bundle){
        this.mainActivity = mainActivity;
        this.bundle = bundle;
    }


    // add items into spinner dynamically
    public void addItemsOnSpinner(List<CubeXML> cubes) {

        fromSpinner = (Spinner) this.mainActivity.findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) this.mainActivity.findViewById(R.id.toSpinner);

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
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                double value = rateList.get(arg2);
                Log.i("Selected", String.valueOf(value));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Get selected item in spinner list
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                double value = rateList.get(arg2);
                Log.i("Selected", String.valueOf(value));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        long selectedId = toSpinner.getSelectedItemId();

        outState.putLong("param", selectedId);
        // do this for each or your Spinner
        // You might consider using Bundle.putStringArray() instead
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize all your visual fields
        if (savedInstanceState != null) {
            Long selectedId = savedInstanceState.getLong("param");
            selectSpinnerItemByValue(toSpinner,selectedId);
        }
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

}
