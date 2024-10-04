package com.skedgo.sqlite

import android.database.Cursor
import io.reactivex.Observable
import io.reactivex.functions.Function

/**
 * Provides some util methods related to [Cursor].
 */
object Cursors {
    /**
     * Emits all the available rows of a [Cursor].
     * This should be composed with [Observable.flatMap].
     */
    @JvmStatic
    fun flattenCursor(): Function<Cursor, Observable<Cursor>> {
        return Function<Cursor, Observable<Cursor>> { cursor -> Observable.fromIterable(RxCursorIterable.from(cursor)) }
    }
}