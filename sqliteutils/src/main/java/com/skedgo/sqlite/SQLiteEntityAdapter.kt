package com.skedgo.sqlite

import android.content.ContentValues
import android.database.Cursor

interface SQLiteEntityAdapter<E> {
    fun toEntity(cursor: Cursor): E

    fun toContentValues(entity: E): ContentValues
}