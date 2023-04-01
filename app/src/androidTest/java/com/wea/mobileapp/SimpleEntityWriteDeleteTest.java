package com.wea.mobileapp;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.wea.local.DBHandler;
import com.wea.models.CMACMessage;
import com.wea.models.CollectedDeviceData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityWriteDeleteTest {
    private DBHandler dbHandler;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHandler = new DBHandler(context);
    }

    @After
    public void closeDb() throws IOException {
        dbHandler.close();
    }

    @Test
    public void writeAlertDelete() throws Exception {
        CMACMessage cmac = new CMACMessage();
        cmac.setMessageNumber("123");
        CollectedDeviceData data = new CollectedDeviceData(cmac, false, false);
        dbHandler.addNewRecord(data, "test");
        dbHandler.removeCMACAlert("123");
        assert dbHandler.getAllRows().size() == 0;
    }
}
