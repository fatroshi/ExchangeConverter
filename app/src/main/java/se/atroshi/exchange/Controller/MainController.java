package se.atroshi.exchange.Controller;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.util.Date;
import se.atroshi.exchange.Design.*;
import se.atroshi.exchange.Exchange.CubeXmlPullParser;
import se.atroshi.exchange.FileStream.Database;
import se.atroshi.exchange.FileStream.StreamFromFile;
import se.atroshi.exchange.MainActivity;
import se.atroshi.exchange.Settings.SettingOptions;

/**
 * Created by Farhad on 18/11/15.
 */
public class MainController {

    private String tag = "MainController";
    private MainActivity mainActivity;
    private Database db;
    private CubeXmlPullParser parser;
    private StreamFromFile streamFromFile;
    private Gui gui;


    public MainController(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        // Connect to database
        this.db = new Database(this.mainActivity, Database.XML_PARSER);
        //
        this.gui = new Gui(mainActivity,this);
        //
        parser = new CubeXmlPullParser();
        //
        streamFromFile = null;

    }

    /**
     * Check if we need to update the app by getting xml data from url
     * Populates the spinners
     * @param isUpdate
     * @param updateTimeInterval
     */
    public void update(boolean isUpdate, int updateTimeInterval){
            if (db.isEmpty()) {
                //
                Log.i(tag, "The database is empty we need stream url...");
                showToast("Database is empty...");
                //
                this.updateApp();
            } else {

                if(isUpdate) {

                    Date oldDate = db.getTimeStamp();
                    Date newDate = new Date();
                    long hours = differenceInHours(oldDate, newDate);
                    //int updateTimeInterval = 24;
                    if (hours > updateTimeInterval) {
                        Log.i(tag, "We need to update our data");
                        this.updateApp();
                    }else{
                        gui.addItemsOnSpinner(db.getCubes());
                    }
                }else{
                    gui.addItemsOnSpinner(db.getCubes());
                }

            }
    }

    /**
     * Updated the app by streaming xml from url
     */
    public void updateApp(){
        // Check if we have internet
        if(isOnline()) {
            // Stream xml from url, pars data, save in db, update gui
            streamFromFile = new StreamFromFile(this.mainActivity, parser, gui);
            streamFromFile.execute();
            showToast("App has been updated");
        }else{
            showToast("Sorry no internet connection...");

            if(db.getCubes().size() > 0){
                showToast("Using old database...");
                gui.addItemsOnSpinner(db.getCubes());
            }else{
                showToast("Please connect to the internet...");
            }
        }
    }

    /**
     *
     * @param startDate
     * @param endDate
     */

    /**
     * I used to compare two dates and get the difference in hours
     * @param startDate
     * @param endDate
     * @return
     */
    public long differenceInHours(Date startDate, Date endDate){

        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        return different/hoursInMilli;

    }

    /**
     * Check if we are connected to the internet
     * @return true if we are connected, false if we are not
     */
    public boolean isOnline() {
        String context = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(context);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Shows messages in the app
     * @param msg
     */
    private void showToast(String msg) {
        Toast toast = Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Calls a method in gui to display the result
     */
    public void showConvertedResult(){
        this.gui.showResult();
    }

    /**
     * Returns the gui
     * @return Gui object
     */
    public Gui getGui(){
        return this.gui;
    }

    /**
     * Returns the the object
     * @return StreamFile object
     */
    public StreamFromFile getStreamFromFile(){
        return this.streamFromFile;
    }

}
