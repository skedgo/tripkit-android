package com.skedgo.tripkit

import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

internal class OnSubscribeLoadTransportModes(private val databaseHelper: SQLiteOpenHelper) :
    ObservableOnSubscribe<Cursor> {
    @Throws(Exception::class)
    override fun subscribe(emitter: ObservableEmitter<Cursor>) {
        var cursor: Cursor? = null
        try {
            val database = databaseHelper.readableDatabase
            cursor = database.rawQuery("select * from " + Tables.TRANSPORT_MODES.name, null)
            emitter.onNext(cursor)
            if (!emitter.isDisposed) {
                emitter.onComplete()
            }
        } catch (e: Exception) {
            emitter.onError(e)
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
    }
}