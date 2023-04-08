package com.wea.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wea.models.CMACMessage;
import com.wea.models.CollectedDeviceData;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "cmac_device_local";
    private static final int DB_VERSION = 1;
    private static final String CMAC_ALERT_TABLE_NAME = "cmac_alert";
    private static final String CMAC_MESSAGE_NO_COL = "messageNumber";
    private static final String CMAC_URI_NO_COL = "uri";
    private static final String CMAC_DATETIME_NO_COL = "timeReceived";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + CMAC_ALERT_TABLE_NAME + " ("
                + CMAC_MESSAGE_NO_COL + " TEXT PRIMARY KEY NOT NULL, "
                + CMAC_URI_NO_COL + " TEXT,"
                + CMAC_DATETIME_NO_COL + " TEXT)";
        db.execSQL(query);
    }

    public void addNewRecord(CollectedDeviceData collectedDeviceData, String uri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CMAC_MESSAGE_NO_COL, Integer.toString(collectedDeviceData.getMessageNumber(), 16));
        values.put(CMAC_URI_NO_COL, uri);
        values.put(CMAC_DATETIME_NO_COL, collectedDeviceData.getTimeReceived());

        db.insert(CMAC_ALERT_TABLE_NAME, null, values);
        db.close();
    }

    public boolean removeCMACAlert(String cmacMessageNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.delete(CMAC_ALERT_TABLE_NAME, "messageNumber" + "=" + cmacMessageNo, null) > 0;
    }

    public List<String> readCMACS() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCMAC = db.rawQuery("SELECT * FROM " + CMAC_ALERT_TABLE_NAME, null);

        List<String> cmacModalArrayList = new ArrayList<>();

        if (cursorCMAC.moveToFirst()) {
            do {
                cmacModalArrayList.add(cursorCMAC.getString(0));
            } while (cursorCMAC.moveToNext());
        }

        cursorCMAC.close();
        return cmacModalArrayList;
    }

    public List<SavedDataModel> getAllRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorCMAC = db.rawQuery("SELECT * FROM " + CMAC_ALERT_TABLE_NAME, null);

        List<SavedDataModel> savedDataList = new ArrayList<>();

        if (cursorCMAC.moveToFirst()) {
            do {
                SavedDataModel model = new SavedDataModel();
                model.setMessageNumber(cursorCMAC.getString(0));
                model.setUri(cursorCMAC.getString(1));
                model.setDateTime(cursorCMAC.getString(2));
                savedDataList.add(model);
            } while (cursorCMAC.moveToNext());
        }

        cursorCMAC.close();
        return savedDataList;
    }

    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CMAC_ALERT_TABLE_NAME);
        onCreate(db);
    }

}