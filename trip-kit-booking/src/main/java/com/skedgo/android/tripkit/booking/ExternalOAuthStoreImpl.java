package com.skedgo.android.tripkit.booking;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import skedgo.sqlite.Cursors;
import skedgo.sqlite.SQLiteEntityAdapter;

import static com.skedgo.android.tripkit.booking.ExternalOAuthTable.EXTERNAL_AUTHS;

public class ExternalOAuthStoreImpl implements ExternalOAuthStore {

  private final Context context;
  private final SQLiteOpenHelper databaseHelper;
  private final SQLiteEntityAdapter<ExternalOAuth> entityAdapter;

  public ExternalOAuthStoreImpl(@NonNull Context context,
                                @NonNull SQLiteOpenHelper databaseHelper,
                                @NonNull SQLiteEntityAdapter<ExternalOAuth> entityAdapter) {
    this.context = context;
    this.databaseHelper = databaseHelper;
    this.entityAdapter = entityAdapter;
  }

  @Override public Observable<ExternalOAuth> updateExternalOauth(final ExternalOAuth externalOAuth) {
    return Observable
        .fromCallable(new Callable<ExternalOAuth>() {
          @Override public ExternalOAuth call() throws Exception {
            final SQLiteDatabase database = databaseHelper.getWritableDatabase();
            try {
              Log.e("**", "save ##DB " + externalOAuth.authServiceId());
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
    return Observable
        .fromCallable(new Callable<Cursor>() {
          @Override public Cursor call() throws Exception {
            final SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM " + EXTERNAL_AUTHS + " WHERE auth_service_id = '" + authId + "'", null);

            database.close();
            return cursor;
          }
        })
        .flatMap(Cursors.flattenCursor())
        .map(new Func1<Cursor, ExternalOAuth>() {
          @Override public ExternalOAuth call(Cursor cursor) {
            return entityAdapter.toEntity(cursor);
          }
        })
        .toSingle()
        .subscribeOn(Schedulers.io());
  }
}
