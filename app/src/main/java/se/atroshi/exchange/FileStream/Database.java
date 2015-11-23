package se.atroshi.exchange.FileStream;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.atroshi.exchange.Exchange.CubeXML;
import se.atroshi.exchange.MainActivity;
import se.atroshi.exchange.Settings.SettingOptions;

/**
 * Created by Farhad on 18/11/15.
 * This class is used for storing data and retrieving data.
 */
public class Database {

    public static final String XML_PARSER = "XmlParser";
    public static final String SETTING_OPTIONS = "Settings";

    private String tag = "Database";
    private List<CubeXML> cubes;
    private SettingOptions settingOptions;
    private String dbName;
    private MainActivity mainActivity;

    public Database(MainActivity mainActivity, String dbName) {
        this.dbName = dbName;
        this.cubes = new ArrayList<>();
        this.mainActivity = mainActivity;

        try {
            dbExists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if database exists
     * @return true if it exists, false if not
     * @throws IOException
     */
    public boolean dbExists() throws IOException {
        boolean dbExists = false;

        File file = this.mainActivity.getFileStreamPath(dbName);

        if(file.exists()){
            dbExists = true;
            Log.i(tag, "DB" + this.dbName + " EXIST");
        }else{
            Log.i(tag, "DB" + this.dbName + " does NOT EXIST");
            // Create database
            createDatabase();

        }
        return dbExists;
    }

    /**
     * Create the database file
     */
    public void createDatabase(){
        try {
            FileOutputStream fos = mainActivity.openFileOutput(this.dbName, Context.MODE_PRIVATE);
            Log.i(tag, "New database " + this.dbName + " created");
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

    /**
     * Load in data depending of choosen database
     * @throws IOException
     */
    public void getData() throws IOException {
        try {
            FileInputStream fis = mainActivity.openFileInput(this.dbName);
            ObjectInputStream is = new ObjectInputStream(fis);
            if(this.dbName.equalsIgnoreCase(Database.XML_PARSER)){
                this.cubes = (List<CubeXML>) is.readObject();
            }else if(this.dbName.equalsIgnoreCase(Database.SETTING_OPTIONS)){
                this.settingOptions = (SettingOptions) is.readObject();

                // Check if we have saved settings
                if(this.settingOptions == null){
                    // First time, settings never saved
                    // create default settings
                    this.settingOptions = new SettingOptions(true,24);
                    // Store in settings db
                    this.insertSettings(this.settingOptions);
                }
            }
            is.close();
            fis.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i(tag, "Could not find given class in file read");
        }

    }

    /**
     * Store data in the database file
     * @param cubes save data is a list of CubeXML objects
     * @throws IOException
     */
    public void insert(List<CubeXML> cubes) throws IOException {
        output("Insert List<CubeXML> cubes");
        FileOutputStream fos = mainActivity.openFileOutput(dbName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(cubes);
        os.close();
        fos.close();
    }

    /**
     * Store data in the database file
     * @param options Saved data is Objects of SettingOptions
     */
    public void insertSettings(SettingOptions options){
        output("Insert settings");
        try {
            FileOutputStream fos = mainActivity.openFileOutput(this.dbName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(options);
            os.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Return data from database
     * @return List of CubeXML
     */
    public List<CubeXML> getCubes(){
        List<CubeXML> tmpCubes = null;
        try {
            // Load in the data from file
            this.getData();
            tmpCubes = this.cubes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpCubes;
    }

    /**
     * Return the date of the stored objects
     * @return date
     */
    public Date getTimeStamp(){

        Date date = null;

        if(this.cubes.size() > 0) {
            date = getCubes().get(0).getDate();
            return date;
        }

        return date;
    }

    /**
     * Check if the returned data is empty
     * Basically checks if we have stored any data before
     * @return
     */
    public boolean isEmpty(){

        boolean isEmpty = true;

        try {
            this.getData();
            if(this.cubes.size() > 0){
                isEmpty = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isEmpty;
    }

    /**
     * Just easier to use than Log.i(tag, "String")
     * @param s message
     */
    public void output(String s){
        Log.i(tag, s);
    }

    /**
     * Return the stored settingOptions
     * @return SettingOptions object that contains the user configs.
     */
    public SettingOptions getSettingOptions(){
        SettingOptions settingOptions = null;

        try {
            // Load in the data from file
            this.getData();

            settingOptions = this.settingOptions;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settingOptions;
    }

}
