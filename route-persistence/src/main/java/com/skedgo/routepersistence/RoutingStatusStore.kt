package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import rx.Completable
import rx.Single
import rx.schedulers.Schedulers.io

class RoutingStatusStore constructor(private val databaseHelper: SQLiteOpenHelper) {
  fun getLastStatus(requestId: String): Single<RoutingStatusDto> {
    return Single
        .fromCallable {
          val cursor = databaseHelper.readableDatabase.query(RoutingStatusContract.ROUTING_STATUS,
              arrayOf(RoutingStatusContract.STATUS, RoutingStatusContract.MESSAGE),
              "${RoutingStatusContract.REQUEST_ID} = ?",
              arrayOf(requestId),
              null, null, null
          )
          cursor.moveToFirst()
          val status = cursor.getString(cursor.getColumnIndex(RoutingStatusContract.STATUS))
          val message = cursor.getString(cursor.getColumnIndex(RoutingStatusContract.MESSAGE))
          cursor.close()
          RoutingStatusDto(status, message)
        }
        .subscribeOn(io())
  }

  fun updateStatus(requestId: String, status: RoutingStatusDto): Completable {
    return Completable
        .fromAction {
          val contentValues = ContentValues()
          contentValues.put(RoutingStatusContract.STATUS, status.first)
          contentValues.put(RoutingStatusContract.REQUEST_ID, requestId)
          contentValues.put(RoutingStatusContract.MESSAGE, status.second)
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
