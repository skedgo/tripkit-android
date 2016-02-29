package com.skedgo.android.tripkit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.skedgo.android.common.content.DatabaseTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

    // To trigger table creation.
    databaseHelper.getReadableDatabase().close();
  }

  @Test public void RegionsTableExists() {
    checkTable(Tables.REGIONS);
  }

  @Test public void TransportModesTableExists() {
    checkTable(Tables.TRANSPORT_MODES);
  }

  @After public void after() {
    databaseHelper.close();
    final File databasePath = context.getDatabasePath(databaseName);
    assertTrue(databasePath.delete());
  }

  private void checkTable(DatabaseTable table) {
    final SQLiteDatabase database = databaseHelper.getReadableDatabase();
    final Cursor cursor = database.rawQuery("SELECT * FROM " + table.getName(), null);
    cursor.close();
    database.close();
  }
}