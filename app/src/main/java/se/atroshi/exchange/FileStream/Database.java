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


    public boolean dbExists() throws IOException {
        boolean dbExists = false;

        File file = this.mainActivity.getFileStreamPath(dbName);

        if(file.exists()){
            dbExists = true;
            Log.i(tag, "File does EXIST");
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

    public void getData() throws IOException {

        try {
            FileInputStream fis = mainActivity.openFileInput(this.dbName);
            ObjectInputStream is = new ObjectInputStream(fis);
            if(this.dbName.equalsIgnoreCase("XmlParser")){
                this.cubes = (List<CubeXML>) is.readObject();
            }else if(this.dbName.equalsIgnoreCase("Settings")){
                this.settingOptions = (SettingOptions) is.readObject();
            }

            is.close();
            fis.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i(tag, "Could not find given class in file read");
        }

    }

    public void insert(List<CubeXML> cubes) throws IOException {
        output("Insert List<CubeXML> cubes");
        FileOutputStream fos = mainActivity.openFileOutput(dbName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(cubes);
        os.close();
        fos.close();
    }

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

    public Date getTimeStamp(){

        Date date = null;

        if(this.cubes.size() > 0) {
            date = getCubes().get(0).getDate();
            return date;
        }

        return date;
    }

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

    public void output(String s){
        Log.i(tag, s);
    }

    public SettingOptions getSettingOptions(){
        return this.settingOptions;
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }


}
