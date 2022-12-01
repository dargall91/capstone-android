package com.wea.local;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.snatik.polygon.Point;
import com.snatik.polygon.Polygon;

import android.widget.Toast;

public class LocationUtils {

    private static Activity globalActivity;
    private static Context globalContext;
    private static LocationRequest globalLocationRequest;

    /**
     * Initializes Location Request
     */
    private static void init() {
        globalLocationRequest = LocationRequest.create();
        globalLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        globalLocationRequest.setInterval(5000);
        globalLocationRequest.setFastestInterval(2000);
    }

    /**
     * Method to get the GPS Location of the device.
     * CURRENTLY THE LOCATION IS PRINTED TO LOGCAT.
     */
    public static void getGPSLocation(Context context, Activity activity) {
        globalActivity = activity;
        globalContext = context;
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(globalLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(globalActivity).removeLocationUpdates(this);

                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int index = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();

                                System.out.println("GETTING DEVICE LOCATION");
                                System.out.println(latitude);
                                System.out.println(longitude);
                            }

                        }
                    }, Looper.getMainLooper());
                } else {
                    turnOnGPS();
                }
            } else {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    /**
     * Checks to see if GPS is enabled on Android device
     *
     * @return A boolean for gps enabled
     */
    private static boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) globalContext.getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isEnabled;
    }

    /**
     * Helper method to give user a dialog box to turn on
     * their location services if it is determined they are
     * turned off.
     */
    private static void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(globalLocationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(globalContext.getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(globalContext, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(globalActivity, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    /**
     * Checks to see if a point is inside a given polygon.
     *
     * @return boolean for if point is inside polygon.
     */
    public static boolean isInsideArea() {

        Double[] xCoords = {40.842226, 40.829498, 40.833394, 40.84768, 40.858716};
        Double[] yCoords = {14.211753, 14.229262, 14.26617, 14.278701, 14.27715};
        Double[] myPoint = {40.8518, 14.2681};

        Polygon.Builder p = new Polygon.Builder();

        for (int i = 0; i < xCoords.length; i++) {
            p.addVertex(new Point(xCoords[i], yCoords[i]));
        }
        p.close();
        Polygon poly = p.build();

        Point point = new Point(myPoint[0], myPoint[1]);

        return poly.contains(point);
    }
}
