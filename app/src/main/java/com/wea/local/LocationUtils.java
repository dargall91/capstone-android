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
import com.wea.models.Coordinate;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LocationUtils {

    private static Activity globalActivity;
    private static Context globalContext;
    private static LocationRequest globalLocationRequest;

    /**
     * Initializes Location Request
     */
    public static void init(Context context, Activity activity) {
        globalActivity = activity;
        globalContext = context;
        globalLocationRequest = LocationRequest.create();
        globalLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        globalLocationRequest.setInterval(5000);
        globalLocationRequest.setFastestInterval(2000);
        turnOnGPS();
    }

    /**
     * Method to get the GPS Location of the device.
     * CURRENTLY THE LOCATION IS PRINTED TO LOGCAT.
     */
    public static Coordinate getGPSLocation() {
        AtomicReference<Coordinate> coordinate = new AtomicReference<>();

        boolean gpsServices = isGPSEnabled();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(globalContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsServices) {
                    LocationServices.getFusedLocationProviderClient(globalActivity).requestLocationUpdates(globalLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(globalActivity).removeLocationUpdates(this);

                            if (locationResult != null && locationResult.getLocations().size() > 0) {
                                int index = locationResult.getLocations().size() - 1;
                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();
                                coordinate.set(new Coordinate(latitude, longitude));

                                System.out.println("GETTING DEVICE LOCATION");
                                System.out.println(latitude);
                                System.out.println(longitude);
                            }
                        }
                    }, Looper.getMainLooper());
                } else {
                    turnOnGPS();
                    gpsServices = isGPSEnabled();
                }
            } else {
                globalActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        return coordinate.get();
    }

    /**
     * Checks to see if GPS is enabled on Android device
     *
     * @return A boolean for gps enabled
     */
    public static boolean isGPSEnabled() {
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

        result.addOnCompleteListener(task -> {

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
        });
    }

    /**
     * Checks to see if a point is inside a given polygon.
     *
     * @return boolean for if point is inside polygon.
     */
    public static boolean isInsideArea(String coords, Double[] myPoint) {

        String[] coordsSplit = coords.split(" ");

        ArrayList x = new ArrayList();
        ArrayList y = new ArrayList();

        for (int i = 0; i < coordsSplit.length; i++){
            String[] s = coordsSplit[i].split(",");
            x.add(Double.parseDouble(s[0]));
            y.add(Double.parseDouble(s[1]));
        }

        Polygon.Builder p = new Polygon.Builder();

        for (int i = 0; i < x.size(); i++) {
            p.addVertex(new Point((Double)x.get(i), (Double)y.get(i)));
        }
        p.close();
        Polygon poly = p.build();

        Point point = new Point(myPoint[0], myPoint[1]);

        return poly.contains(point);
    }
}
