package com.buzzhives.android.tripplanner.data;

import android.database.Cursor;

import rx.functions.Func1;

public interface CursorToEntityConverter<E> extends Func1<Cursor, E> {}