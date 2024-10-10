/*
 * Copyright (c) SkedGo 2013
 */
package com.skedgo.tripkit.common.util

import android.text.TextUtils
import java.util.Arrays
import java.util.regex.Pattern

object StringUtils {
    @JvmStatic
    fun firstNonEmpty(vararg values: String?): String? {
        for (v in values) {
            if (!TextUtils.isEmpty(v)) {
                return v
            }
        }

        return null
    }

    @JvmStatic
    fun capitalizeFirst(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }

        return str[0].uppercaseChar().toString() + str.substring(1)
    }

    @JvmStatic
    fun makeArgsString(argc: Int): String {
        val args = arrayOfNulls<String>(argc)
        Arrays.fill(args, "?")
        val result = TextUtils.join(",", args)
        return result
    }

    /*Join an array of strings by default dilimeter, which is comma*/
    fun join(strings: ArrayList<String>?): String {
        return join(strings, ",")
    }

    /**
     * Joins the elements of the provided List into a single String containing the provided elements.
     *
     * @param strings   The List of values to join together, may be null
     * @param separator The separator character to use
     * @return The joined String, empty if null List input
     */
    fun join(strings: List<String>?, separator: String): String {
        var s = ""
        if (strings != null) {
            for (item in strings) {
                s += item + separator
            }

            val pos = s.lastIndexOf(separator)
            s = s.substring(0, pos).trim { it <= ' ' }
        }

        return s
    }

    fun extractMajorMinorVersion(version: String): String {
        try {
            val pattern = Pattern.compile("(\\d+\\.\\d+)")
            val matcher = pattern.matcher(version)
            return if (matcher.find()) {
                matcher.group(1).toString()
            } else {
                version // Return the original string if no match is found
            }
        } catch (e: Exception) {
            return version // Return the original string if there's an error
        }
    }
}
