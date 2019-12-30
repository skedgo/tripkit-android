package com.skedgo.sqlite;

import android.database.Cursor;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Provides some util methods related to {@link Cursor}.
 */
public final class Cursors {
  private Cursors() {}

  /**
   * Emits all the available rows of a {@link Cursor}.
   * This should be composed with {@link Observable#flatMap(Function)}.
   */
  public static Function<Cursor, Observable<Cursor>> flattenCursor() {
    return new Function<Cursor, Observable<Cursor>>() {
      @Override public Observable<Cursor> apply(Cursor cursor) {
        return Observable.fromIterable(RxCursorIterable.from(cursor));
      }
    };
  }
}