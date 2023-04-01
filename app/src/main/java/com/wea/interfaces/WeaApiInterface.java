package com.wea.interfaces;

import android.content.Context;
import android.content.res.AssetManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickaroo.tikxml.TikXml;
import com.wea.models.CollectedDeviceData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WeaApiInterface {
    private static final OkHttpClient client = new OkHttpClient();
    private static TikXml tikXml = new TikXml.Builder().exceptionOnUnreadXml(false).build();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String HTTP = "http://";
    private static final String PORT_PATH = ":8080/wea/api/";
    private static String SERVER_IP;

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
            } catch (IOException ignored) { }
        }
    }

    /**
     * executes a GET request that expects a single result
     * @param classType
     * @param endpoint
     * @param <T>
     * @return
     */
    public static <T> T getSingleXmlResult(Class<T> classType, String endpoint) {
        AtomicReference<T> result = new AtomicReference<>();

        Thread thread = new Thread(() -> {
            try {
                Request request = new Request.Builder()
                        .url(HTTP + SERVER_IP + PORT_PATH + endpoint)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    result.set(tikXml.read(response.body().source(), classType));
                } else {
                    result.set(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();
    }

    /**
     * executes a GET request that expects a single result
     * @param classType
     * @param endpoint
     * @param <T>
     * @return
     */
    public static <T> T getSingleJsonResult(Class<T> classType, String endpoint) {
        AtomicReference<T> result = new AtomicReference<>();

        Thread thread = new Thread(() -> {
            try {
                Request request = new Request.Builder()
                        .url(HTTP + SERVER_IP + PORT_PATH + endpoint)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    result.set(mapper.readValue(response.body().string(), classType));
                } else {
                    result.set(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();
    }

    /**
     * executes a POST request with a payload and gets the newly created resources URI location
     * @param endpoint the endpoint path
     * @param payload the POST payload object
     * @param <T> Class type of the payload
     */
    public static <T> String postGetUri(String endpoint, T payload) {
        AtomicReference<String> uriLocation = new AtomicReference<>();

        Thread thread = new Thread(() -> {
            try {
                String payloadString = mapper.writer().writeValueAsString(payload);
                RequestBody body = RequestBody.create(payloadString, MediaType.parse("application/json"));

                Request request = new Request.Builder()
                        .url(HTTP + SERVER_IP + PORT_PATH + endpoint)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    uriLocation.set(response.header("location"));
                } else {
                    uriLocation.set(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return uriLocation.get();
    }
}
