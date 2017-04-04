package skedgo.tripkit.analytics

data class MiniSegment(
    val mode: String,
    val duration: Long
) {
  init {
    require(duration >= 0) {
      "Duration must be greater than or equal to 0.. Found: $duration"
    }
  }
}