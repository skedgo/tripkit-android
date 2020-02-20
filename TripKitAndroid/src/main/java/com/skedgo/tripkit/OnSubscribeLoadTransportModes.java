package com.skedgo.tripkit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

final class OnSubscribeLoadTransportModes implements ObservableOnSubscribe<Cursor> {
  private final SQLiteOpenHelper databaseHelper;

  OnSubscribeLoadTransportModes(SQLiteOpenHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  @Override
  public void subscribe(ObservableEmitter<Cursor> emitter) throws Exception {
    Cursor cursor = null;
    try {
      final SQLiteDatabase database = databaseHelper.getReadableDatabase();
      cursor = database.rawQuery("select * from " + Tables.TRANSPORT_MODES.getName(), null);
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