package com.skedgo.sqlite

import android.text.TextUtils

object UniqueIndices {
    fun of(tableName: String, vararg fields: DatabaseField?): String {
        // 'UI' stands for 'Unique Index'.
        val indexName = tableName + "_UI_" + TextUtils.join("_", fields)
        return "CREATE UNIQUE INDEX $indexName ON $tableName (" + TextUtils.join(
            ", ",
            fields
        ) + ");"
    }
}