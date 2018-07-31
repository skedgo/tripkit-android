package skedgo.tripkit.routingstatus

sealed class Status(val message: String? = null) {
  class InProgress : Status()
  class Completed : Status()
  class Error(message: String?) : Status(message)
}
