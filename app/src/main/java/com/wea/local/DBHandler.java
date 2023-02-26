package com.wea.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wea.local.model.CMACMessageModel;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "cmac_device_local";
    private static final int DB_VERSION = 1;
    private static final String CMAC_MESSAGE_TABLE_NAME = "cmac_message";
    private static final String CMAC_ALERT_TABLE_NAME = "cmac_alert";
    private static final String CMAC_MESSAGE_NO_COL = "messageNumber";
    private static final String CMAC_SENDER_NO_COL = "sender";
    private static final String CMAC_DATETIME_NO_COL = "sentDateTime";
    private static final String CMAC_MESSAGE_TYPE_NO_COL = "messageType";
    private static final String CMAC_CAP_ID_NO_COL = "capIdentifier";
    private static final String DEVICE_LOCATION_COL = "DeviceLocation";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + CMAC_ALERT_TABLE_NAME + " ("
                + CMAC_MESSAGE_NO_COL + " TEXT PRIMARY KEY NOT NULL)";
        db.execSQL(query);
    }

    public void addNewCMACAlert(String cmacMessageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CMAC_MESSAGE_NO_COL, cmacMessageNo);

        db.insert(CMAC_ALERT_TABLE_NAME, null, values);

        db.close();
    }

    public void removeCMACAlert(String cmacMessageNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "WHERE " + CMAC_MESSAGE_NO_COL + " = ";
        String[] args = {cmacMessageNo};

        db.delete(db.toString(), query, args);
    }

    public void insertNewDeviceLocation(String cmacMessageNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }

    public ArrayList<CMACMessageModel> readCMACS() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCMAC = db.rawQuery("SELECT * FROM " + CMAC_MESSAGE_TABLE_NAME, null);

        ArrayList<CMACMessageModel> cmacModalArrayList = new ArrayList<>();

        if (cursorCMAC.moveToFirst()) {
            do {
                cmacModalArrayList.add(new CMACMessageModel(cursorCMAC.getString(1)));
            } while (cursorCMAC.moveToNext());
        }

        cursorCMAC.close();
        return cmacModalArrayList;
    }

    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CMAC_MESSAGE_TABLE_NAME);
        onCreate(db);
    }

}