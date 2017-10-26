package skedgo.tripkit.configuration

sealed class Key {
  /**
   * An API key is necessary to use TripKit's services, such as A-2-B routing,
   * and all-day routing. In order to obtain an API key,
   * you can sign up [here](https://tripgo.3scale.net).
   */
  data class ApiKey(val value: String) : Key()

  @Deprecated(message = "Should use ApiKey instead")
  data class RegionEligibility(val value: String) : Key()
}
