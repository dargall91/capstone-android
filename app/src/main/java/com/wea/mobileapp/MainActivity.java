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
import com.wea.mobileapp.databinding.ActivityMainBinding;
import com.wea.local.DBHandler;
import com.wea.models.CMACMessage;
import com.wea.models.CollectedDeviceData;
import com.wea.models.Coordinate;

import android.view.Menu;
import android.view.MenuItem;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private DBHandler dbHandler;

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

            //if no entries are found in the db, the list will be size 0
            if (receivedMessages != null && receivedMessages.size() != 0) {
                endpoint += "?receivedMessages=" + String.join(",", receivedMessages);
            }

            CMACMessage message = WeaApiInterface.getSingleXmlResult(CMACMessage.class, endpoint);

            //if no message is received
            if (message == null) {
                Snackbar.make(view, "No incoming messages found", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            } else {
                Snackbar.make(view, "Retrieved message from server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            CollectedDeviceData deviceData = new CollectedDeviceData(message, LocationUtils.isGPSEnabled(),
                    LocationUtils.isInsideArea(message));

            if (!deviceData.isReceivedInside()) {
                String polygon = message.getAlertInfo().getAlertAreaList().get(0).getPolygon();
                Coordinate userLocation = LocationUtils.getGPSLocation();
                double distanceFromPolygon = DistanceOutsidePolygon.distanceFromPolygon(userLocation, polygon);
                double distanceFormatted = Double.parseDouble(String.format("%.2f", distanceFromPolygon));
                deviceData.setDistanceFromPolygon(distanceFormatted);
            }

            deviceData.setOptedOut(userOptedOut(message));

            Random rand = new Random();
            int randomSleep = rand.nextInt(100) + 1;

            //simulate a short random time between receiving and displaying the message
            try {
                Thread.sleep(randomSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            getWeaAlertDialog(message, deviceData, view).show();
        };
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

                    if (locationUri != null) {
                        dbHandler.addNewRecord(deviceData, locationUri.replace("http://localhost:8080/wea/api/", ""));
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
            deviceData.setTimeDisplayed(OffsetDateTime.now(ZoneOffset.UTC).withNano(0).toString());
            //set vibration and sound effects
            long[] vibrationPatter = {200, 1900, 150};
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPatter, 0));
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        });

        return weaAlertDialog;
    }

    /**
     * Since we don't have any settings programme to determine this, just opt out 10% of the time or if message is test
     * @return true if opted out, otherwise false
     */
    private boolean userOptedOut(CMACMessage message) {
        if (message.getMessageType() == null || message.getMessageType().equalsIgnoreCase("test")) {
            return true;
        }

        return Math.random() <= 0.1;
    }
}