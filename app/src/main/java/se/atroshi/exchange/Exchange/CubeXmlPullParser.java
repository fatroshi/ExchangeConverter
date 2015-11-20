package se.atroshi.exchange.Exchange;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        String date = null;

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
                            if(tmpDate == null){

                                tmpDate = xpp.getAttributeValue(null, KEY_DATE);
                                date = tmpDate + " HH:mm:ss";
                                //date = tmpDate;
                                SimpleDateFormat sdf = new SimpleDateFormat(date);
                                date = sdf.format(new Date());
                                Log.i(tag,date);
                            }
                            String currency = xpp.getAttributeValue(null, KEY_CURRENCY);
                            String rate = xpp.getAttributeValue(null, KEY_RATE);
                            if(currency != null) {
                                // Create new object
                                double doubleRate = Double.parseDouble(rate);
                                cubes.add(new CubeXML(date,currency, doubleRate));
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

}


