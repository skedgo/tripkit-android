package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import rx.Completable
import rx.Single
import rx.schedulers.Schedulers.io

class RoutingStatusStore constructor(private val databaseHelper: SQLiteOpenHelper) {
  fun getLastStatus(requestId: String): Single<String> {
    return Single
        .fromCallable {
          val cursor = databaseHelper.readableDatabase.query(RoutingStatusContract.ROUTING_STATUS,
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
        .subscribeOn(io())
  }

  fun updateStatus(requestId: String, status: String): Completable {
    return Completable
        .fromAction {
          val contentValues = ContentValues()
          contentValues.put(RoutingStatusContract.STATUS, status)
          contentValues.put(RoutingStatusContract.REQUEST_ID, requestId)
          databaseHelper.writableDatabase.insertWithOnConflict(
              RoutingStatusContract.ROUTING_STATUS,
              null,
              contentValues,
              SQLiteDatabase.CONFLICT_REPLACE
          )
        }
        .subscribeOn(io())
  }
}
