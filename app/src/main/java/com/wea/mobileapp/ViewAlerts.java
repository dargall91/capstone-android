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
        CMACMessageModel CMACMes1 = new CMACMessageModel();
        CMACMessageModel CMACMes2 = new CMACMessageModel();

        CMACMes1.setMessageNumber("12345");
        CMACMes2.setMessageNumber("34546");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alerts);

        alertModalArrayList = new ArrayList<>();
        //dbHandler = new DBHandler(ViewAlerts.this);

        alertModalArrayList.add(CMACMes1);
        alertModalArrayList.add(CMACMes2);

        cmacrvAdapter = new CMACRVAdapter(alertModalArrayList, ViewAlerts.this);
        alertsRV = findViewById(R.id.idRVAlerts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewAlerts.this, RecyclerView.VERTICAL, false);
        alertsRV.setLayoutManager(linearLayoutManager);

        alertsRV.setAdapter(cmacrvAdapter);
    }
}