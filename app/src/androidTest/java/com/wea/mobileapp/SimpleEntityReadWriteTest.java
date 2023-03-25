package com.wea.mobileapp;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.wea.local.DBHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {

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
    public void writeAlertAndRead() throws Exception {
    }
}
