package se.atroshi.exchange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;
import se.atroshi.exchange.Controller.MainController;
import se.atroshi.exchange.Design.Gui;

public class MainActivity extends AppCompatActivity {

    private final String tag = "MainActivity";
    private MainController controller;
    private Spinner toSpinner;
    private Spinner fromSpinner;
    private Gui gui;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        controller = new MainController(this);
        gui = this.controller.getGui();
        toSpinner = gui.getToSpinner();
        fromSpinner = gui.getFromSpinner();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
//        savedInstanceState.putBoolean("MyBoolean", true);
//        savedInstanceState.putDouble("myDouble", 1.9);
//        savedInstanceState.putInt("MyInt", 1);
//        savedInstanceState.putString("MyString", "Welcome back to Android");
//        // etc.
        Spinner toSpinner = this.controller.getGui().getToSpinner();
        Spinner fromSpinner = this.controller.getGui().getFromSpinner();

        savedInstanceState.putInt("toSpinner", toSpinner.getSelectedItemPosition());
        savedInstanceState.putInt("fromSpinner", fromSpinner.getSelectedItemPosition());

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
//        boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
//        double myDouble = savedInstanceState.getDouble("myDouble");
//        int myInt = savedInstanceState.getInt("MyInt");
//        String myString = savedInstanceState.getString("MyString");
        int toSpinnerPosition =savedInstanceState.getInt("toSpinner");
        int fromSpinnerPosition = savedInstanceState.getInt("fromSpinner");

        Spinner toSpinner = this.controller.getGui().getToSpinner();
        Spinner fromSpinner = this.controller.getGui().getFromSpinner();

        Log.i(tag, "To spinner: " +  String.valueOf(toSpinnerPosition));
        Log.i(tag, "From spinner: " + String.valueOf(fromSpinnerPosition));

        this.controller.getGui().selectSpinnerItemByValue(this.toSpinner, toSpinnerPosition);
        this.controller.getGui().selectSpinnerItemByValue(this.fromSpinner, fromSpinnerPosition);

        showToast("Hello world");
    }


    @Override
    protected void onStart(){
        super.onStart();
        // Check if we have old data
        // Update database, gui if we have old data
        controller.update();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
