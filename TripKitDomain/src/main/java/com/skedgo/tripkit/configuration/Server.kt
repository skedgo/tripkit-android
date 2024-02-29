package com.skedgo.tripkit.configuration

enum class DefaultServer(val value: String) {
    ApiTripGo("https://api.tripgo.com/v1/"),
    BigBang("https://galaxies.skedgo.com/lab/beta/satapp/")
}

open class ServerConfiguration(
    var apiTripGoUrl: String = DefaultServer.ApiTripGo.value,
    var bigBangUrl: String = DefaultServer.BigBang.value
)

/**
 * Singleton object that manages server configurations for the application.
 *
 * This manager allows for setting and retrieving server URLs throughout the application.
 * It provides a centralized point of configuration, making it easy to adjust server URLs
 * for different environments (e.g., development, staging, production) or build variants.
 *
 * Usage:
 * - Access default server URLs via `ServerManager.configuration`.
 * - Customize server URLs using `ServerManager.customizeConfiguration(...)`.
 *
 * Example:
 * ```
 * // Accessing default URLs
 * val defaultApiUrl = ServerManager.configuration.apiTripGoUrl
 * val defaultBigBangUrl = ServerManager.configuration.bigBangUrl
 *
 * // Customizing URLs
 * ServerManager.customizeConfiguration(apiTripGoUrl = "https://api.newtripgo.com/v2/", bigBangUrl = "https://new.bigbang.url/")
 * ```
 *
 * Note: Customizations to the server URLs should ideally be done during application initialization
 * to ensure consistent use of the URLs throughout the application lifecycle.
 */
object ServerManager {

    var configuration: ServerConfiguration = ServerConfiguration()

    fun setCustomConfiguration(apiTripGoUrl: String? = null, bigBangUrl: String? = null) {
        apiTripGoUrl?.let { configuration.apiTripGoUrl = it }
        bigBangUrl?.let { configuration.bigBangUrl = it }
    }

}