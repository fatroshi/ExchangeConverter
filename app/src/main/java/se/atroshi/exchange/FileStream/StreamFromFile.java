package se.atroshi.exchange.FileStream;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import se.atroshi.exchange.Exchange.CubeXmlPullParser;
import se.atroshi.exchange.Design.Gui;
import se.atroshi.exchange.MainActivity;

/**
 * Created by Farhad on 11/11/15.
 */
public class StreamFromFile extends AsyncTask<URL,Integer,Long> {

    private final String urlString = "http://maceo.sth.kth.se/Home/";
    private final String fileName = "eurofxref";

    private String tag = "StreamFromFile";
    private MainActivity mainActivity;
    private CubeXmlPullParser cubeParser;
    private Gui gui;

    public StreamFromFile(MainActivity currentActivity, CubeXmlPullParser cubeParser, Gui gui) {
        this.mainActivity = currentActivity;
        this.cubeParser = cubeParser;
        this.gui = gui;
    }


    @Override
    protected Long doInBackground(URL... params) {
        URLConnection connection;
        BufferedReader in;
        try{
            URL url = new URL(urlString+fileName);
            connection = url.openConnection();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // XML PARSING
            this.cubeParser.getXmlFromStream(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(Long result)
    {
        this.gui.addItemsOnSpinner(this.cubeParser.getCubes());

        try {
            Database db = new Database(mainActivity);
            db.insert(this.cubeParser.getCubes());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void output(String s){
        Log.i(this.tag, s);
    }

}