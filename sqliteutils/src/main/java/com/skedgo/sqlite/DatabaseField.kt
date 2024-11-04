package com.skedgo.sqlite

class DatabaseField @JvmOverloads constructor(
    @JvmField val name: String,
    @JvmField val type: String,
    @JvmField val constraint: String? = null
) {
    override fun toString(): String {
        return name
    }
}