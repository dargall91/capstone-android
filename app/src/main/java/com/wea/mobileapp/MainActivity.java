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
import com.wea.local.LocationUtils;
import com.wea.local.model.CMACMessageModel;
import com.wea.local.model.CollectedUserData;
import com.wea.mobileapp.databinding.ActivityMainBinding;
import com.wea.local.DBHandler;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList messageArr = new ArrayList();
    private DBHandler dbHandler = new DBHandler(getBaseContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        WeaApiInterface.setServerIp(getApplicationContext());

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
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Retrieving message from server...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                CMACMessageModel message = WeaApiInterface.getSingleResult(CMACMessageModel.class, "getMessage");

                //if no message is received
                if (message == null) {
                    Snackbar.make(view, "No incoming messages found", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                } else {
                    Snackbar.make(view, "Retrieved message from server", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                CollectedUserData userData = new CollectedUserData("048151", message);

                dbHandler.getWritableDatabase();
                dbHandler.addNewCMACAlert(message.getMessageNumber());
                HistoryFragment.setText(dbHandler.readCMACS());

                Random rand = new Random();
                int randomSleep = rand.nextInt(100) + 1;

                //simulate a short random time between receiving and displaying the message
                try {
                    Thread.sleep(randomSleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getWeaAlertDialog(message, userData, view).show();

                LocationUtils.getGPSLocation(MainActivity.this, MainActivity.this);

                String coords = "40.842226,14.211753 40.829498,14.229262, 40.833394,14.26617 40.84768,14.278701 40.858716,14.27715";
                Double[] myPoint = {40.8518, 14.2681};

                boolean inside = LocationUtils.isInsideArea(coords, myPoint);
                System.out.println("CHECKING INSIDE POLYGON");
                System.out.println(inside);
            }
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
    private AlertDialog getWeaAlertDialog(CMACMessageModel message, CollectedUserData userData, View view) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.emergency_alert);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        AlertDialog.Builder weaAlertBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(message.getShortMessage("english"))
                .setIcon(R.drawable.alert_icon)
                .setMessage(message.getLongMessage("english"))
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    vibrator.cancel();
                    mediaPlayer.stop();
                    //Handle device data upload on close
                    AtomicBoolean success = new AtomicBoolean(false);
                    String locationUri = WeaApiInterface.postGetUri("upload", userData);

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
            //Handle setting device on message display
            userData.setDisplayData("", message);
            //set vibration and sound effects
            long[] vibrationPatter = {200, 1900, 150};
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPatter, 0));
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        });

        return weaAlertDialog;
    }
}