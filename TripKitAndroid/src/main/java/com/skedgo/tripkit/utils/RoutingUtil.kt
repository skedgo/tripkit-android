package com.skedgo.tripkit.utils

import android.os.Build
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.skedgo.tripkit.data.RoutingInfo
import com.skedgo.tripkit.data.RoutingJsonResult

// Function to get the Android version
fun getAndroidVersion(): String {
    return "Android " + Build.VERSION.RELEASE
}


// Function to create the JSON file with error handling
fun createRoutingJsonFile(
    routingJsonString: String?,
    versionName: String?
): Result<RoutingJsonResult> {
    return try {
        if (routingJsonString.isNullOrEmpty() || versionName.isNullOrEmpty()) {
            throw IllegalArgumentException("Routing JSON string or version name is null or empty")
        }

        val routingInfo = RoutingInfo(
            url = routingJsonString,
            os = getAndroidVersion(),
            version = versionName
        )

        val gson = Gson()
        val jsonString = gson.toJson(routingInfo)

        // Convert the JSON string to a byte array
        val fileContent = jsonString.toByteArray()

        // Encode the byte array to a base64 string
        val base64EncodedString = Base64.encodeToString(fileContent, Base64.NO_WRAP)

        Result.success(RoutingJsonResult(fileContent, base64EncodedString))
    } catch (e: Exception) {
        Log.e("createRoutingJsonFile", "Error creating routing JSON file", e)
        Result.failure(e)
    }
}