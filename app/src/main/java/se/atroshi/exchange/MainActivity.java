package se.atroshi.exchange;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;
import se.atroshi.exchange.Controller.MainController;

public class MainActivity extends AppCompatActivity {

    private final String tag = "MainActivity";
    private MainController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        controller = new MainController(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        Spinner toSpinner = this.controller.getGui().getToSpinner();
        Spinner fromSpinner = this.controller.getGui().getFromSpinner();
        savedInstanceState.putInt("toSpinner", toSpinner.getSelectedItemPosition());
        savedInstanceState.putInt("fromSpinner", fromSpinner.getSelectedItemPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int toSpinnerPosition =savedInstanceState.getInt("toSpinner");
        int fromSpinnerPosition = savedInstanceState.getInt("fromSpinner");

        Spinner toSpinner = this.controller.getGui().getToSpinner();
        Spinner fromSpinner = this.controller.getGui().getFromSpinner();

        this.controller.getGui().selectSpinnerItemByValue(toSpinner, toSpinnerPosition);
        this.controller.getGui().selectSpinnerItemByValue(fromSpinner, fromSpinnerPosition);
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
