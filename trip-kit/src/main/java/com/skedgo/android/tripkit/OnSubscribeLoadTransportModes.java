package com.skedgo.android.tripkit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import rx.Observable;
import rx.Subscriber;

final class OnSubscribeLoadTransportModes implements Observable.OnSubscribe<Cursor> {
  private final SQLiteOpenHelper databaseHelper;

  OnSubscribeLoadTransportModes(SQLiteOpenHelper databaseHelper) {
    this.databaseHelper = databaseHelper;
  }

  @Override
  public void call(Subscriber<? super Cursor> subscriber) {
    try {
      final SQLiteDatabase database = databaseHelper.getReadableDatabase();
      final Cursor cursor = database.rawQuery("select * from " + Tables.TRANSPORT_MODES.getName(), null);
      subscriber.onNext(cursor);
      subscriber.onCompleted();
    } catch (Exception e) {
      subscriber.onError(e);
    }
  }
}