package se.atroshi.exchange.FileStream;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import se.atroshi.exchange.Exchange.CubeXML;
import se.atroshi.exchange.MainActivity;
import se.atroshi.exchange.Settings;

/**
 * Created by Farhad on 18/11/15.
 */
public class Database {

    private String tag = "Database";
    private List<CubeXML> cubes;
    private Settings settings;
    private String dbName;
    private MainActivity mainActivity;

    public Database(MainActivity mainActivity) {
        this.dbName = "db";
        this.cubes = new ArrayList<>();
        this.mainActivity = mainActivity;

        try {
            dbExists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean dbExists() throws IOException {
        boolean dbExists = false;

        File file = this.mainActivity.getFileStreamPath(dbName);

        if(file.exists()){
            dbExists = true;
            Log.i(tag, "File does EXIST");
            // Load in data
            try {
                setData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.i(tag, "File does NOT EXIST");
            // Create database
            createDatabase();

        }
        return dbExists;
    }

    public void createDatabase(){
        try {
            FileOutputStream fos = mainActivity.openFileOutput(this.dbName, Context.MODE_PRIVATE);
            Log.i(tag, "New database file created");
            try{
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }catch (FileNotFoundException e){
            // File could not be created
            Log.i(tag, "Database could not be created");
            e.printStackTrace();
        }
    }

    public void setData() throws IOException {

        try {
            FileInputStream fis = mainActivity.openFileInput(this.dbName);
            ObjectInputStream is = new ObjectInputStream(fis);
            this.cubes = (List<CubeXML>) is.readObject();
            is.close();
            fis.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i(tag, "Could not find given class in file read");
        }

    }

    public void insert(List<CubeXML> cubes) throws IOException {
        output("Insert method");
        FileOutputStream fos = mainActivity.openFileOutput(dbName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(cubes);
        os.close();
        fos.close();
    }

    public  List<CubeXML> getCubes(){
        return this.cubes;
    }

    public Date getTimeStamp(){
        if(this.cubes.size() > 0) {
            String strDate = this.cubes.get(0).getDate();
            DateFormat format = new SimpleDateFormat(strDate, Locale.ENGLISH);
            Date date = null;
            try {
                date = format.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date;
        }else{
            return null;
        }


    }

    public void output(String s){
        Log.i(tag, s);
    }
}
