package se.atroshi.exchange.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private Database db;
    private CubeXmlPullParser parser;
    private StreamFromFile streamFromFile;
    private Gui gui;


    public MainController(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        // Connect to database
        //this.db = new Database(this.mainActivity);

        //
        gui = new Gui(mainActivity);

        parser = new CubeXmlPullParser();
        streamFromFile = new StreamFromFile(this.mainActivity,parser,gui);
        streamFromFile.execute();

    }


    public boolean update(){
        boolean update = false;

        // Get current date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();

        Log.i(tag, String.valueOf(date));

        // Compare to date in db


        return update;
    }




}
