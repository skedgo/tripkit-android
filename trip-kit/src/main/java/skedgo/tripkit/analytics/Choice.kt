package skedgo.tripkit.analytics

data class Choice(
    val price: Float,
    val score: Float,
    val carbon: Float,
    val hassle: Float,
    val calories: Float,
    val segments: List<MiniSegment>,
    val selected: Boolean,
    val visibility: Visibility,
    val arrivalTime: Long,
    val departureTime: Long
)
