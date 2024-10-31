package com.skedgo.sqlite

import android.database.Cursor

class RxCursorIterable(private val mIterableCursor: Cursor) : Iterable<Cursor?> {
    override fun iterator(): MutableIterator<Cursor> {
        return RxCursorIterator.from(mIterableCursor)
    }

    internal class RxCursorIterator(private val mCursor: Cursor) : MutableIterator<Cursor> {
        override fun hasNext(): Boolean {
            return !mCursor.isClosed && mCursor.moveToNext()
        }

        override fun next(): Cursor {
            return mCursor
        }

        override fun remove() {
        }

        companion object {
            fun from(cursor: Cursor): MutableIterator<Cursor> {
                return RxCursorIterator(cursor)
            }
        }
    }

    companion object {
        fun from(c: Cursor): RxCursorIterable {
            return RxCursorIterable(c)
        }
    }
}