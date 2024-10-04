package com.skedgo.sqlite

import android.database.sqlite.SQLiteDatabase

class DatabaseTable(
    val name: String,
    val fields: Array<DatabaseField>,
    private vararg val customScripts: String
) {
    private var fieldNames: Array<String>? = null

    override fun toString(): String {
        return name
    }

    fun getFieldNames(): Array<String> {
        if (fieldNames == null) {
            fieldNames = Array(fields.size) { i ->
                fields[i].name
            }
        }
        return fieldNames!!
    }

    fun getDropSql(): String {
        return "DROP TABLE IF EXISTS $name"
    }

    fun getPostCreateSql(): Array<out String> {
        return customScripts
    }

    fun getCreateSql(): String {
        val sqlTextBuilder = StringBuilder()
            .append("CREATE TABLE ").append(name).append(" (")

        fields.forEachIndexed { index, field ->
            if (index > 0) sqlTextBuilder.append(", ")
            sqlTextBuilder.append(field.name)
                .append(" ")
                .append(field.type)
            field.constraint?.let {
                sqlTextBuilder.append(" ").append(it)
            }
        }

        sqlTextBuilder.append(")")
        return sqlTextBuilder.toString()
    }

    fun create(database: SQLiteDatabase) {
        database.execSQL(getCreateSql())
        customScripts.forEach { script ->
            database.execSQL(script)
        }
    }
}