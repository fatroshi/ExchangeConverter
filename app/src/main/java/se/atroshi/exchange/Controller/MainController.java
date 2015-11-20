package se.atroshi.exchange.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private Database db;
    private CubeXmlPullParser parser;
    private StreamFromFile streamFromFile;
    private Gui gui;


    public MainController(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        // Connect to database
        this.db = new Database(this.mainActivity);

        //
        gui = new Gui(mainActivity);
        parser = new CubeXmlPullParser();
        streamFromFile = new StreamFromFile(this.mainActivity,parser,gui);
        streamFromFile.execute();


    }


    public boolean update(){
        boolean update = false;

        if(db.iseEmpty()){
            //
            Log.i(tag,"The database is empty we need stream url...");
            // Stream xml from url, pars data, save in db, update gui
            streamFromFile = new StreamFromFile(this.mainActivity,parser,gui);
            streamFromFile.execute();
        }else{

            Date oldDate = db.getTimeStamp();
            Date newDate = new Date();

            long hours = differenceInHours(oldDate,newDate);

            if(hours > 24){
                Log.i(tag, "Wer need to update our data");
            }

        }


        return update;
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

}
