package se.atroshi.exchange.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    private Gui gui;


    public MainController(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        // Connect to database
        this.db = new Database(this.mainActivity);

        //
        gui = new Gui(mainActivity);

        start();

    }

    public void start(){
        // Skriv om den har lite flummugt... borde forst kolla update sen.. kolla om vi ar anslutna till internet.
        // for lasa fil gor vi i tva fall, da internet inte finns och nar datan ar fortfarande farskt.

        if(isOnline()){
            // We are connected to the internet
            // Check date, need to update?
            if(update() || db.getCubes().size() == 0){
                // We nee to update our database, newer xml file available
                this.parser = new CubeXmlPullParser();
                // Stream the file, pars data, store in db, update gui with new information
                StreamFromFile sff = new StreamFromFile(this.mainActivity,this.parser,this.gui);
                sff.execute();
            }else{
                this.gui.addItemsOnSpinner(db.getCubes());
            }
        }else{
            if(db.getCubes().size() == 0){
                // Tell user that we need to be connected to the interner
            }else{
                this.gui.addItemsOnSpinner(db.getCubes());
                // Tell user old data
                // well... if data is still less than 24h old.. maybe we should not tell anything
                // don't know ! call update method again?!
            }
        }
    }

    public boolean update(){
        boolean update = false;
        if(db.getCubes().size() > 0) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();
            System.out.println(dateFormat.format(currentDate));

            if (currentDate.before(db.getTimeStamp())) {
               update = true;
            }
        }
        return update;
    }

    public boolean isOnline() {
        String context = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(context);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
