package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import rx.Completable

@Suppress("IllegalIdentifier")
@RunWith(RobolectricTestRunner::class)
class ManualTripsContractTest {
  @Test
  fun `should not emit errors creating table`() {
    Completable
        .fromAction {
          val database = SQLiteDatabase.create(null)
          ManualTripsContract.create(database)
        }
        .test()
        .assertNoErrors()
  }
}