package com.skedgo.tripkit

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.sqlite.DatabaseTable
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegionDatabaseHelperTest {
    private var databaseHelper: RegionDatabaseHelper? = null
    private var databaseName: String? = null
    private var context: Context? = null

    @Before
    fun before() {
        databaseName = RegionDatabaseHelperTest::class.java.simpleName
        context = InstrumentationRegistry.getInstrumentation().targetContext
        databaseHelper = RegionDatabaseHelper(
            context,
            databaseName
        )

        // To trigger table creation.
        databaseHelper!!.readableDatabase.close()
    }

    @Test
    fun RegionsTableExists() {
        checkTable(Tables.REGIONS)
    }

    @Test
    fun TransportModesTableExists() {
        checkTable(Tables.TRANSPORT_MODES)
    }

    @After
    fun after() {
        databaseHelper?.close()
        val databasePath = context!!.getDatabasePath(databaseName)
        Assert.assertTrue(databasePath.delete())
    }

    private fun checkTable(table: DatabaseTable) {
        val database = databaseHelper!!.readableDatabase
        val cursor = database.rawQuery("SELECT * FROM " + table.name, null)
        cursor.close()
        database.close()
    }
}