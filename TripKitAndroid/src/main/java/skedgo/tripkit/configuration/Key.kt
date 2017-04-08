package skedgo.tripkit.configuration

sealed class Key {
  data class ApiKey(val value: String) : Key()
  data class RegionEligibility(val value: String) : Key()
}
