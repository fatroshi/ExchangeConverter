package se.atroshi.exchange.Exchange;
import android.text.format.Time;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import se.atroshi.exchange.FileStream.StreamFromFile;

/**
 * Created by Farhad on 11/11/15.
 */

public class CubeXmlPullParser implements Serializable {

    private final String tag = "CubeXmlPullParser";
    private final String KEY_RATE = "rate";
    private final String KEY_CURRENCY = "currency";
    private final String KEY_CUBE = "cube";
    private final String KEY_DATE = "time";

    private List<CubeXML> cubes = new ArrayList<CubeXML>();
    public List<CubeXML> getCubes(){
        return cubes;
    }

    public void getXmlFromStream (BufferedReader reader){

        String tmpDate = null;
        String strDate = null;
        Date date = null;

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            // point the parser to our file.
            xpp.setInput(reader);
            // get initial eventType
            int eventType = xpp.getEventType();

            // process tag while not reaching the end of document
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    // at start of document: START_DOCUMENT
                    case XmlPullParser.START_DOCUMENT:
                        //study = new Study();
                        break;

                    // at start of a tag: START_TAG
                    case XmlPullParser.START_TAG:
                        // get tag name
                        String tagName = xpp.getName();

                        // if <Cube>, get attribute: 'currency'
                        if(tagName.equalsIgnoreCase(KEY_CUBE)) {
                            if(strDate == null){
                                strDate = xpp.getAttributeValue(null, KEY_DATE);
                            }

                            String currency = xpp.getAttributeValue(null, KEY_CURRENCY);
                            String rate = xpp.getAttributeValue(null, KEY_RATE);
                            if(currency != null) {

                                // Create new object
                                date = createDate(strDate);                             // Create date
                                double doubleRate = Double.parseDouble(rate);           // Convert string to double
                                cubes.add(new CubeXML(date,currency, doubleRate));      // Create object and store in list
                            }
                        }
                        break;
                }
                // jump to next event
                eventType = xpp.next();
            }

            for(CubeXML cube: cubes){
                Log.i("Object", cube.toString());
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Date createDate(String strDate){

        Date date = null;

        // Get time
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
        String localTime = dateformat.format(currentLocalTime);

        // Concat strings
        String tmpDate = strDate + " " + localTime;
        //
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = format.parse(tmpDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}


