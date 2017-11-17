package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import rx.Completable
import rx.Single
import rx.schedulers.Schedulers
import skedgo.sqlite.Cursors

class RoutingStatusStore constructor(private val dbHelper: SQLiteOpenHelper) {
  fun getLastStatus(requestId: String): Single<String> {
    return Single
        .fromCallable {
          val cursor = dbHelper.readableDatabase.query(RoutingStatusContract.ROUTING_STATUS,
              arrayOf(RoutingStatusContract.STATUS),
              "${RoutingStatusContract.REQUEST_ID} = ?",
              arrayOf(requestId),
              null, null, null
          )
          cursor.moveToFirst()
          val status = cursor.getString(cursor.getColumnIndex(RoutingStatusContract.STATUS))
          cursor.close()
          status
        }
        .subscribeOn(Schedulers.io())
  }

  fun updateStatus(requestId: String, status: String): Completable {
    return Completable
        .fromAction {
          val contentValues = ContentValues()
          contentValues.put(RoutingStatusContract.STATUS, status)
          contentValues.put(RoutingStatusContract.REQUEST_ID, requestId)
          dbHelper.writableDatabase.insertWithOnConflict(
              RoutingStatusContract.ROUTING_STATUS,
              null,
              contentValues,
              SQLiteDatabase.CONFLICT_REPLACE
          )
        }
        .subscribeOn(Schedulers.io())
  }

}