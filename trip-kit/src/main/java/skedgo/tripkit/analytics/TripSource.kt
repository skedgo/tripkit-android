package skedgo.tripkit.analytics

enum class TripSource(val type: String) {
  Agenda("agenda"),
  Booking("booking"),
  External("external"),
  Favorite("favorite"),
  Manual("manual"),
  Unknown("unknown")
}
