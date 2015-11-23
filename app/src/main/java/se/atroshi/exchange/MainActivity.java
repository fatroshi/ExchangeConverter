package se.atroshi.exchange;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;
import se.atroshi.exchange.Controller.MainController;
import se.atroshi.exchange.FileStream.Database;
import se.atroshi.exchange.Settings.SettingOptions;

/**
 * Created by Farhad on 18/11/15.
 * This is the mainActivity and the start point of the application.
 * Handles the different lifecycle states of the application and also sending data to other activities.
 *
 * The controller class is also created here.
 */

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_SETTINGS = 100;          // Used as an id for the data received from SettingsActivy
    private final String tag = "MainActivity";              // Tag, using it in Log.i(tag ....
    private MainController controller;                      // App controller class


    /**
     * Creates the application controller
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        controller = new MainController(this);
    }

    /**
     * Load settings for the app
     * Check if we need to update the database and gui
     */
    @Override
    protected void onStart(){
        super.onStart();
        // Get user settings
        Database dbSettings = new Database(this, Database.SETTING_OPTIONS);
        int updateTimeInterval =  dbSettings.getSettingOptions().getUpdateInterval();
        boolean update = dbSettings.getSettingOptions().isUpdate();

        // Check if we have old data
        // Update database, gui if we have old data
        controller.update(update, updateTimeInterval);
    }

    /**
     * Cancel download if the application is not current app in the phone (example. user has pressed back btn)
     */
    @Override
    protected void onPause(){
        super.onPause();

        try{
            if(this.controller.getStreamFromFile() != null) {
                //this.controller.getStreamFromFile().getTask().cancel(true);
                if(this.controller.getStreamFromFile().getTask().getStatus() == AsyncTask.Status.RUNNING){
                    this.controller.getStreamFromFile().getTask().cancel(true);
                }
            }
        }catch (NullPointerException e){
            showToast("The task is done");
            e.printStackTrace();
        }


    }

    /**
     * Retrieve data from SettingsActivity , store in Database
     * @param requestCode code use for detecting if was sent to this class/methdd
     * @param resultCode  if the result was ok
     * @param data Data sent from from SettingsActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){

        if(requestCode == REQUEST_CODE_SETTINGS){
            if(resultCode == RESULT_OK){
                int seekBarUpdateInterval = data.getIntExtra("seekBarUpdateInterval", 24);
                boolean switchUpdate = data.getBooleanExtra("switchUpdate", true);

                // Create OptionUpdate object
                SettingOptions settingOptions = new SettingOptions(switchUpdate,seekBarUpdateInterval);
                // Update database
                Database dbSettings = new Database(this, Database.SETTING_OPTIONS);
                dbSettings.insertSettings(settingOptions);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Save spinner position before rotating
     * @param savedInstanceState
     */
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

    /**
     * Restore spinners item position (the selected item before rotating)
     * @param savedInstanceState
     */
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Send data to the SettingsActivity, this data is used by the widgets for showing
     * correct status/values.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //showToast("Settings Selected");
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            Database dbSettings = new Database(this, Database.SETTING_OPTIONS);
            boolean switchUpdate = dbSettings.getSettingOptions().isUpdate();
            int seekBarUpdateInterval = dbSettings.getSettingOptions().getUpdateInterval();

            intent.putExtra("seekBarUpdateInterval", seekBarUpdateInterval);
            intent.putExtra("switchUpdate",switchUpdate );
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
