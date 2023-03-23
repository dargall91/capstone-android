package com.wea.mobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.wea.local.DBHandler;
import com.wea.local.model.CMACMessageModel;

import java.util.ArrayList;

public class ViewAlerts extends AppCompatActivity {

    private ArrayList<CMACMessageModel> alertModalArrayList;
    private DBHandler dbHandler;
    private CMACRVAdapter cmacrvAdapter;
    private RecyclerView alertsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alerts);

        alertModalArrayList = new ArrayList<>();
        dbHandler = new DBHandler(ViewAlerts.this);

        cmacrvAdapter = new CMACRVAdapter(alertModalArrayList, ViewAlerts.this);
        alertsRV = findViewById(R.id.idRVAlerts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAlerts.this, RecyclerView.VERTICAL, false);
        alertsRV.setLayoutManager(linearLayoutManager);

        alertsRV.setAdapter(cmacrvAdapter);
    }
}