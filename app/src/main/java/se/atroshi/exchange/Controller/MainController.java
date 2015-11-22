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

/**
 * Created by Farhad on 18/11/15.
 */
public class MainController {

    private String tag = "MainController";
    private MainActivity mainActivity;
    private Bundle bundle;
    private Database db;
    private CubeXmlPullParser parser;
    private StreamFromFile streamFromFile;
    private Gui gui;


    public MainController(MainActivity mainActivity, Bundle bundle){
        this.mainActivity = mainActivity;
        this.bundle = bundle;
        // Connect to database
        this.db = new Database(this.mainActivity);

        //
        gui = new Gui(this.mainActivity, this);
        parser = new CubeXmlPullParser();
    }

    public boolean update(){
        boolean update = false;

        if(db.isEmpty()){
            //
            Log.i(tag, "The database is empty we need stream url...");
            showToast("Database is empty...");
            //
            this.updateApp();
        }else{

            Date oldDate = db.getTimeStamp();
            Date newDate = new Date();

            long hours = differenceInHours(oldDate,newDate);
            if(hours > 24){
                Log.i(tag, "We need to update our data");
                this.updateApp();
            }else{
                gui.addItemsOnSpinner(db.getCubes());
            }

        }

        return update;
    }

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

    public long differenceInHours(Date startDate, Date endDate){

        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        return different/hoursInMilli;

    }

    public boolean isOnline() {
        String context = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(context);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showConvertedResult(){
        this.gui.showResult();
    }
}
