package com.skedgo.tripkit.data

object TripKitKeys {
    private var skedGoApiKey: String? = null
    private var googlePlacesApiKey: String? = null

    fun configureSkedGoApiKey(key: String) {
        skedGoApiKey = key
    }

    fun getSkedGoApiKey() = skedGoApiKey ?: ""

    fun getSkedGoApiKeyWithDefault(default: String) = skedGoApiKey ?: default

    fun configureGooglePlacesApiKey(key: String) {
        googlePlacesApiKey = key
    }

    fun getGooglePlacesApiKey() = googlePlacesApiKey


}