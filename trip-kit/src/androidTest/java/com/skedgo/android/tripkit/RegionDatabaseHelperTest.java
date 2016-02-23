package com.skedgo.android.tripkit;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.File;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RegionDatabaseHelperTest {
  private RegionDatabaseHelper databaseHelper;
  private String databaseName;
  private Context context;

  @Before public void before() {
    databaseName = RegionDatabaseHelperTest.class.getSimpleName();
    context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    databaseHelper = new RegionDatabaseHelper(
        context,
        databaseName
    );
  }

  @After public void after() {
    databaseHelper.close();
    final File databasePath = context.getDatabasePath(databaseName);
    assertTrue(databasePath.delete());
  }
}