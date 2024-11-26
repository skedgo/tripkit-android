package com.skedgo.tripkit.data

object TripKitKeys {
    private var skedGoApiKey: String? = null
    private var googlePlacesApiKey: String? = null
    private var stripeApiKey: String? = null
    private var stripeApiKeySandbox: String? = null
    private var auth0ClientId: String? = null

    fun configureSkedGoApiKey(key: String) {
        skedGoApiKey = key
    }

    fun getSkedGoApiKey() = skedGoApiKey ?: ""

    fun getSkedGoApiKeyWithDefault(default: String) = skedGoApiKey ?: default

    fun configureGooglePlacesApiKey(key: String) {
        googlePlacesApiKey = key
    }

    fun getGooglePlacesApiKey() = googlePlacesApiKey

    fun configureStripeApiKey(key: String) {
        stripeApiKey = key
    }

    fun getStripeApiKey() = stripeApiKey

    fun getStripeApiKeyWithDefault(default: String) = stripeApiKey ?: default

    fun configureStripeApiKeySandbox(key: String) {
        stripeApiKeySandbox = key
    }

    fun getStripeApiKeySandbox() = stripeApiKeySandbox

    fun getStripeApiKeySandboxWithDefault(default: String) = stripeApiKeySandbox ?: default

    fun configureAuth0ClientId(key: String) {
        auth0ClientId = key
    }

    fun getAuth0ClientId() = auth0ClientId

    fun getAuth0ClientId(default: String) = auth0ClientId ?: default

}