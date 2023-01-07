package com.wea.local;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.tickaroo.tikxml.TikXml;
import com.wea.local.model.CollectedUserData;
import com.wea.local.model.CMACMessageModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

/**
 * This class handles the parsing of incoming CMAC messages, storing them into the local database, displaying messages,
 * and uploading the user's data to the server
 */
public class CMACProcessor {
    private static CollectedUserData userData;
    private static String SERVER_IP;
    private static boolean displayDatSet = false;

    /**
     * Sets the ip address of the server. To get the proper Context, this method should be called from MainActivity,
     * and must be called before attempting to parse or upload a message
     *
     * @param context The application context
     */
    public static void setServerIp(Context context) {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        
        try {
            AssetManager assets = context.getAssets();
            inputStreamReader = new InputStreamReader(assets.open("server_address.dat"));
            bufferedReader = new BufferedReader(inputStreamReader);
            SERVER_IP = bufferedReader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) { }
        }
    }

    /**
     * Parses a CMAC message creates the initial user data to be uploaded
     *
     * @return The deserialized CMAC XML object, or null if the object could not be deserialized, of the server ip was
     * never set
     */
    public static CMACMessageModel parseMessage() {
        if (SERVER_IP == null) {
            Log.i("Parse Error", "Server ip not set");
            return null;
        }

        CMACMessageModel cmacMessage = null;

        try {
            URL getMessage = new URL("http://" + SERVER_IP + ":8080/wea/api/getMessage");
            InputStream inputStream = getMessage.openStream();

            BufferedSource source = Okio.buffer(Okio.source(inputStream));
            TikXml parser = new TikXml.Builder().exceptionOnUnreadXml(false).build();

            cmacMessage = parser.read(source, CMACMessageModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        /*
        TODO: once we are able to get location data, the device's geocode should be passed as the
         first parameter. Optionally, we could also remove the location parameter and put the logic
         for determining the current location in the constructor
         */
        userData = new CollectedUserData("048151", cmacMessage);

        //TODO: insert cmacMessage into the database here

        return cmacMessage;
    }

    /**
     * Sets the display data (current location and time) for the user upload data
     *
     * @param context The context of the activity that is displaying the alert
     * @param cmacMessage The message to be displayed
     * @return true if the data is successfully set
     */
    public static boolean setDisplayData(Context context, CMACMessageModel cmacMessage) {
        if (cmacMessage == null) {
            Log.i("Display Error", "Message not yet parsed");
            return false;
        }

        Log.i("Here", "here");

        //TODO: like the constructor, replace this parameter with the current geocode string, or simply move the logic
        // to get the geocode into the method
        userData.setDisplayData("048151", cmacMessage);

        displayDatSet = true;
        return true;
    }

    /**
     * Uploads the collected user data to the server. The data will not be uploaded if the message has not yet been
     * displayed to the user or if the server ip was never set
     *
     * @return True if the user's data was successfully uploaded
     */
    public static boolean uploadUserData() {
        if (!displayDatSet || SERVER_IP == null) {
            Log.i("Upload Error", "Message not yet displayed or server ip not set");
            return false;
        }

        try {
            URL upload = new URL("http://" + SERVER_IP + ":8080/wea/api/upload");
            HttpURLConnection con = (HttpURLConnection) upload.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/xml");
            con.setDoOutput(true);

            BufferedSink sink = Okio.buffer((Sink) new Buffer());
            TikXml parser = new TikXml.Builder().exceptionOnUnreadXml(false).build();
            parser.write(sink, userData);
            con.getOutputStream().write(sink.buffer().readByteArray());

            //Get the location url for this upload: that is, the endpoint where this data can be found
            Map<String, List<String>> map = con.getHeaderFields();
            String locationUrl = map.get("Location").get(0).replace("localhost", SERVER_IP);

            //TODO: store locationUrl in the database here

            Log.i("Upload Location", locationUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
