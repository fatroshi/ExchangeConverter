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
 * This class is used for streaming content from URL, and by using the class CubeXmlParser parsing the xml.
 */
public class StreamFromFile extends AsyncTask<URL,Integer,Long> {

    private final String schoolURL = "http://maceo.sth.kth.se/Home/eurofxref";
    private final String bankURL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private final String urlString = schoolURL  ;


    private String tag = "StreamFromFile";
    private MainActivity mainActivity;
    private CubeXmlPullParser cubeParser;
    private Gui gui;

    private AsyncTask<URL, Integer, Long> task;

    public StreamFromFile(MainActivity currentActivity, CubeXmlPullParser cubeParser, Gui gui) {
        this.mainActivity = currentActivity;
        this.cubeParser = cubeParser;
        this.gui = gui;
        this.task = this;
    }


    public AsyncTask<URL, Integer, Long> getTask(){
        return this.task;
    }

    /**
     * This function is used when whe need to update the database (spinners).
     * Runs as a parallel thread.
     * @param params
     * @return
     */
    @Override
    protected Long doInBackground(URL... params) {
        URLConnection connection;
        BufferedReader in;
        try{
            if(this.isCancelled()) {
                showToast("Task is cancelled");
            }else{
                URL url = new URL(urlString);
                connection = url.openConnection();
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // XML PARSING
                this.cubeParser.getXmlFromStream(in);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * When the method doInBackground is finished this method is called
     * In this method we update the database and gui (spinners)
     * @param result
     */
    protected void onPostExecute(Long result)
    {
        showToast("Download finished");
        try {
            this.gui.addItemsOnSpinner(this.cubeParser.getCubes());
            Database db = new Database(mainActivity,Database.XML_PARSER);
            db.insert(this.cubeParser.getCubes());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Used for displaying messages to the user on the screen
     * @param msg
     */
    private void showToast(String msg) {
        Toast toast = Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT);
        toast.show();
    }


}