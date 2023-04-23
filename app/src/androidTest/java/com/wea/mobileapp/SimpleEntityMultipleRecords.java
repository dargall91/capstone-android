package com.wea.mobileapp;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.wea.local.DBHandler;
import com.wea.models.CMACMessage;
import com.wea.models.CollectedDeviceData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SimpleEntityMultipleRecords {
    private DBHandler dbHandler;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHandler = new DBHandler(context);
    }

    @After
    public void closeDb() throws IOException {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(dbHandler.getDatabaseName());
        dbHandler.close();
    }

    @Test
    public void multipleDatabaseEntry() throws Exception {
        CMACMessage cmac = new CMACMessage();
        CMACMessage cmac_two = new CMACMessage();
        cmac.setMessageNumber("777");
        cmac_two.setMessageNumber("888");
        CollectedDeviceData data = new CollectedDeviceData(cmac, false, false);
        CollectedDeviceData data2 = new CollectedDeviceData(cmac_two, false, false);
        dbHandler.addNewRecord(data, "test");
        dbHandler.addNewRecord(data2, "test2");
        assert dbHandler.getAllRows().size() == 2;
    }
}
