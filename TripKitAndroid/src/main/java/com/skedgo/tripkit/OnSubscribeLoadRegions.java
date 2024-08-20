package com.skedgo.tripkit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

final class OnSubscribeLoadRegions implements ObservableOnSubscribe<Cursor> {
    private final SQLiteOpenHelper databaseHelper;

    OnSubscribeLoadRegions(SQLiteOpenHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
        Cursor cursor = null;
        try {
            final SQLiteDatabase database = databaseHelper.getReadableDatabase();
            cursor = database.rawQuery("select * from " + Tables.REGIONS.getName(), null);
            emitter.onNext(cursor);
            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        } catch (Exception e) {
            emitter.onError(e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

    }
}