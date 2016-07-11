package com.skedgo.android.tripkit.booking;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Single;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import skedgo.sqlite.Cursors;
import skedgo.sqlite.SQLiteEntityAdapter;

import static com.skedgo.android.tripkit.booking.ExternalOAuthTable.EXTERNAL_AUTHS;

public class ExternalOAuthStoreImpl implements ExternalOAuthStore {

  private final SQLiteOpenHelper databaseHelper;
  private final SQLiteEntityAdapter<ExternalOAuth> entityAdapter;

  public ExternalOAuthStoreImpl(@NonNull SQLiteOpenHelper databaseHelper,
                                @NonNull SQLiteEntityAdapter<ExternalOAuth> entityAdapter) {
    this.databaseHelper = databaseHelper;
    this.entityAdapter = entityAdapter;
  }

  @Override public Observable<ExternalOAuth> updateExternalOauth(final ExternalOAuth externalOAuth) {
    return Observable
        .fromCallable(new Callable<ExternalOAuth>() {
          @Override public ExternalOAuth call() throws Exception {
            final SQLiteDatabase database = databaseHelper.getWritableDatabase();
            try {
              database.beginTransaction();

              database.insertWithOnConflict(
                  EXTERNAL_AUTHS.getName(),
                  null,
                  entityAdapter.toContentValues(externalOAuth),
                  SQLiteDatabase.CONFLICT_REPLACE
              );

              database.setTransactionSuccessful();
            } finally {
              database.endTransaction();
              database.close();
            }

            return externalOAuth;
          }
        })
        .subscribeOn(Schedulers.io());
  }

  @Override public Single<ExternalOAuth> getExternalOauth(final String authId) {
    final SQLiteDatabase database = databaseHelper.getReadableDatabase();
    return Observable
        .fromCallable(new Callable<Cursor>() {
          @Override public Cursor call() throws Exception {
            return database.rawQuery("SELECT * FROM " + EXTERNAL_AUTHS + " WHERE auth_service_id = '" + authId + "'", null);
          }
        })
        .flatMap(Cursors.flattenCursor())
        .map(new Func1<Cursor, ExternalOAuth>() {
          @Override public ExternalOAuth call(Cursor cursor) {
            return entityAdapter.toEntity(cursor);
          }
        })
        .doOnCompleted(new Action0() {
          @Override public void call() {
            database.close();
          }
        })
        .toSingle()
        .subscribeOn(Schedulers.io());
  }
}
