package com.skedgo.sqlite;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

public interface SQLiteEntityAdapter<E> {
    @NonNull
    E toEntity(@NonNull Cursor cursor);

    @NonNull
    ContentValues toContentValues(@NonNull E entity);
}