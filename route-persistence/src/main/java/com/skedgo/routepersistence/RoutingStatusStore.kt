package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import skedgo.tripkit.routingstatus.Status

class RoutingStatusStore constructor(private val databaseHelper: SQLiteOpenHelper) {
  fun getLastStatus(requestId: String): Single<Pair<String, String>> {
    return Single
        .fromCallable {
          val cursor = databaseHelper.readableDatabase.query(RoutingStatusContract.ROUTING_STATUS,
              arrayOf(RoutingStatusContract.STATUS, RoutingStatusContract.STATUS_MESSAGE),
              "${RoutingStatusContract.REQUEST_ID} = ?",
              arrayOf(requestId),
              null, null, null
          )
          if (cursor.moveToFirst()) {
              val status = cursor.getString(cursor.getColumnIndex(RoutingStatusContract.STATUS))
              val message = cursor.getString(cursor.getColumnIndex(RoutingStatusContract.STATUS_MESSAGE))
              cursor.close()
              Pair(status, message)
          } else {
              Pair("Error", "Not Started Yet")
          }

        }
        .subscribeOn(Schedulers.io())
  }

  fun updateStatus(requestId: String, status: String, message: String?): Completable {
    return Completable
        .fromAction {
          val contentValues = ContentValues()
          contentValues.put(RoutingStatusContract.STATUS, status)
          contentValues.put(RoutingStatusContract.STATUS_MESSAGE, message)
          contentValues.put(RoutingStatusContract.REQUEST_ID, requestId)
          databaseHelper.writableDatabase.insertWithOnConflict(
              RoutingStatusContract.ROUTING_STATUS,
              null,
              contentValues,
              SQLiteDatabase.CONFLICT_REPLACE
          )
        }
        .subscribeOn(Schedulers.io())
  }
}