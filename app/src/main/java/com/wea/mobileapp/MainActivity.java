package com.wea.mobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.wea.interfaces.WeaApiInterface;
import com.wea.local.DistanceOutsidePolygon;
import com.wea.local.LocationUtils;
import com.wea.local.model.CMACMessageModel;
import com.wea.mobileapp.databinding.ActivityMainBinding;
import com.wea.local.DBHandler;
import com.wea.models.CMACMessage;
import com.wea.models.CollectedDeviceData;
import com.wea.models.Coordinate;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableRow;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private DBHandler dbHandler;
    private CMACRVAdapter adapter;
    private ViewAlerts history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        dbHandler = new DBHandler(getBaseContext());
        dbHandler.onUpgrade(dbHandler.getReadableDatabase(), 0, 1);

        WeaApiInterface.setServerIp(getApplicationContext());
        LocationUtils.init(getApplicationContext(), this);

        binding.getMessageButton.setOnClickListener(getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Defines the OnClickListener for the getMessageButton
     * Clicking the button will trigger the following procedure:
     * 1. Get a message from the API (if no message is found, take no no further action)
     * 2. Timestamp the time it was received
     * 3. Display the message
     * 4. Timestamp the time it was displayed
     * 5. Determine if the user was inside the target area
     * 6. Upload the collected data to the api
     *
     * @return The OnClickListener event
     */
    private View.OnClickListener getMessage() {
        return view -> {
            Snackbar.make(view, "Retrieving message from server...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            String endpoint = "getMessage";

            List<String> receivedMessages = dbHandler.readCMACS();
            //if no entries are found in the db, the list will contain a single null element
            if (receivedMessages.get(0) != null) {
                endpoint += "?receivedMessages=" + String.join(",", receivedMessages);
            }

            CMACMessage message = WeaApiInterface.getSingleResult(CMACMessage.class, endpoint);

            //if no message is received
            if (message == null) {
                Snackbar.make(view, "No incoming messages found", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            } else {
                Snackbar.make(view, "Retrieved message from server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            CollectedDeviceData deviceData = new CollectedDeviceData(message, LocationUtils.isGPSEnabled(), isInsideArea(message));

            Random rand = new Random();
            int randomSleep = rand.nextInt(100) + 1;

            //simulate a short random time between receiving and displaying the message
            try {
                Thread.sleep(randomSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            getWeaAlertDialog(message, deviceData, view).show();

            boolean locationServicesOn = LocationUtils.isGPSEnabled();

            System.out.println("PRINTING LOCATION SERVICES");
            System.out.println(locationServicesOn);

            String coords = "40.842226,14.211753 40.829498,14.229262, 40.833394,14.26617 40.84768,14.278701 40.858716,14.27715";
            Double[] myPoint = {40.8518, 14.2681};

            Double[] info = DistanceOutsidePolygon.closestPointOnPolygon(myPoint, coords);
            System.out.println("CHECKING DISTANCE FROM POLYGON");
            System.out.println(info[0] + " " + info[1] +  " " + info[2]);

            boolean inside = LocationUtils.isInsideArea(coords, myPoint);
            System.out.println("CHECKING INSIDE POLYGON");
            System.out.println(inside);
        };
    }

    private void loadCMAC() {
        CMACMessageModel[] cmacMessage;
        dbHandler.getReadableDatabase();
        cmacMessage = dbHandler.readCMACS().toArray(new CMACMessageModel[0]);
        int rows = cmacMessage.length;

        for(int i = 0; i < rows; i++){
            final TableRow row = new TableRow(this);
            row.setId(i);
        }
    }

    /**
     * Creates and returns an AlertDialog that displays a WEA message. The Dialog also handles setting the user data
     * for when the alert is displayed as well as upload the data to the server
     *
     * @param message The CMAC Message to be displayed, this array should contain only one element
     * @param view        The view hosting the AlertDialog
     * @return A WEA AlertDialog
     */
    private AlertDialog getWeaAlertDialog(CMACMessage message, CollectedDeviceData deviceData, View view) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.emergency_alert);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        AlertDialog.Builder weaAlertBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(message.getAlertInfo().getAlertTextList().get(0).getShortMessage())
                .setIcon(R.drawable.alert_icon)
                .setMessage(message.getAlertInfo().getAlertTextList().get(0).getLongMessage())
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    vibrator.cancel();
                    mediaPlayer.stop();

                    String locationUri = WeaApiInterface.postGetUri("upload", deviceData);
                    dbHandler.addNewRecord(deviceData, locationUri);

                    if (locationUri != null) {
                        Snackbar.make(view, "Successfully uploaded user data", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(view, "Failed to upload user data", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

        final AlertDialog weaAlertDialog = weaAlertBuilder.create();
        weaAlertDialog.setOnShowListener(dialogInterface -> {
            //set message is inside area
            deviceData.setMessagePresented(true);
            //set vibration and sound effects
            long[] vibrationPatter = {200, 1900, 150};
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPatter, 0));
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        });

        return weaAlertDialog;
    }

    private boolean isInsideArea(CMACMessage message) {
        String polygon = message.getAlertInfo().getAlertAreaList().get(0).getPolygon();
        if (polygon == null) {
            return false;
        }

        Coordinate currentLocation = LocationUtils.getGPSLocation();
        if (currentLocation == null) {
            return false;
        }

        Double[] currentCoordinates = { currentLocation.getLatitude(), currentLocation.getLongitude() };
        return LocationUtils.isInsideArea(polygon, currentCoordinates);
    }
}