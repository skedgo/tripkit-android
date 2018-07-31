package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import rx.Completable
import rx.Single
import rx.schedulers.Schedulers.io

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
          try {
            cursor.moveToFirst()
            val status = cursor.getString(cursor.getColumnIndex(RoutingStatusContract.STATUS))
            val message = cursor.getString(cursor.getColumnIndex(RoutingStatusContract.STATUS_MESSAGE))
            Pair(status, message)
          } catch (e: Exception) {
            error(e)
          } finally {
            cursor.close()
          }
        }
        .subscribeOn(io())
  }

  fun updateStatus(requestId: String, status: String, message: String?): Completable {
    return Completable
        .fromAction {
          val contentValues = ContentValues()
          contentValues.put(RoutingStatusContract.STATUS, status)
          contentValues.put(RoutingStatusContract.STATUS_MESSAGE, message ?: "")
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
